package com.example.threadsapp.view.profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentEditProfileBinding
import com.example.threadsapp.util.Utils
import com.example.threadsapp.viewModel.profileViewModel.EditProfileViewModel
import com.example.threadsapp.viewModel.profileViewModel.PhotoViewModel
import com.example.threadsapp.viewModel.profileViewModel.UserInfoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private var PICK_IMAGE_REQUEST  = 1
    private var selectedImageUri: Uri? = null
    private val getDataViewModel: UserInfoViewModel by viewModels()
    private val editProfileViewModel: EditProfileViewModel by viewModels()
    private val photoViewModel: PhotoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        showUserData()
    }

    private fun setupNavigation() {
        binding.closeBtn.setOnClickListener {
            navigateToProfileFragment()
        }
        binding.doneBtn.setOnClickListener {
            saveData()
            setPhoto()
        }
        binding.editPhotoBtn.setOnClickListener {
            showImageOptionsBottomSheet()
        }
        binding.switchBtn.setOnClickListener {
            // TODO: Handle switch button action
        }
    }

    private fun showUserData() {
        getDataViewModel.getData(
            onSuccess = { userProfile ->
                if (userProfile != null) {
                    val usernameText = userProfile.username ?: ""
                    val fullName = userProfile.full_name ?: ""
                    val bioText = userProfile.bio ?: ""
                    val websiteText = userProfile.website ?: ""

                    val usernameEditable = Editable.Factory.getInstance().newEditable(usernameText)
                    val fullNameEditable = Editable.Factory.getInstance().newEditable(fullName)
                    val bioEditable = Editable.Factory.getInstance().newEditable(bioText)
                    val websiteEditable = Editable.Factory.getInstance().newEditable(websiteText)

                    binding.etUsername.text = usernameEditable
                    binding.etName.text = fullNameEditable
                    binding.etBio.text = bioEditable
                    binding.etLink.text = websiteEditable
                }
            },
            onError = {
                showToast("Unable to load profile data")
            }
        )
    }

    private fun setPhoto() {
        photoViewModel.editPhoto(requireContext(),
            onSuccess = {
                showToast("Photo has been edited")
            },
            onError = {
                showToast("Try again")
            })
    }

    private fun saveData() {
        val username = binding.etUsername.text.toString()
        val bio = binding.etBio.text.toString()
        val fullName = binding.etName.text.toString()
        val link = binding.etLink.text.toString()

        editProfileViewModel.updateProfile(
            username, fullName, bio, link,
            onSuccess = {
                showToast("Data has been changed")
            },
            onError = {
                showToast("Please, try again")
            }
        )
    }

    private fun navigateToProfileFragment() {
        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
    }

    private fun showImageOptionsBottomSheet() {
        val dialog = createBottomSheetDialog()
        val newPhotoView = dialog.findViewById<View>(R.id.new_photo_btn)
        val removePhoto = dialog.findViewById<View>(R.id.remove_btn)

        newPhotoView?.setOnClickListener {
            dialog.dismiss()
            chooseImage()
        }
        removePhoto?.setOnClickListener {
            selectedImageUri = null
            loadDefaultProfilePhoto()
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("ResourceType")
    private fun createBottomSheetDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.image_bottom_sheet, null)
        dialog.setContentView(view)
        return dialog
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun loadDefaultProfilePhoto() {
        Glide.with(this).load(R.drawable.profile_photo).into(binding.profilePhoto)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            Utils.selectedImageUri = selectedImageUri
            Glide.with(this)
                .load(selectedImageUri)
                .circleCrop()
                .into(binding.profilePhoto)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}