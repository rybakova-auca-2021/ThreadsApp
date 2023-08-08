package com.example.threadsapp.view.profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentEditProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private var PICK_IMAGE_REQUEST  = 1
    private var selectedImageUri: Uri? = null

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
    }

    private fun setupNavigation() {
        binding.closeBtn.setOnClickListener {
            navigateToProfileFragment()
        }
        binding.doneBtn.setOnClickListener {
            // TODO: Handle done button action
        }
        binding.editPhotoBtn.setOnClickListener {
            showImageOptionsBottomSheet()
        }
        binding.switchBtn.setOnClickListener {
            // TODO: Handle switch button action
        }
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
            Glide.with(this)
                .load(selectedImageUri)
                .circleCrop()
                .into(binding.profilePhoto)
        }
    }
}