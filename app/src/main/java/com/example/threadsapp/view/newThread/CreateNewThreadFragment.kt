package com.example.threadsapp.view.newThread

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentCreateNewThreadBinding
import com.example.threadsapp.util.Utils
import com.example.threadsapp.viewModel.newThreadViewModel.CreateThreadViewModel

class CreateNewThreadFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewThreadBinding
    private var PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private var selectedVideoUri: Uri? = null
    private val viewModel: CreateThreadViewModel by viewModels()
    private val userInfoViewModel: UserInfoViewModel by viewModels()
    private var selectedCommentOption: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupCharacterCount()
        showUserData()
        userInfoViewModel.getInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setupNavigation() {
        binding.addMedia.setOnClickListener {
            chooseMedia()
        }
        binding.textReply.setOnClickListener {
            showPopupMenu(it)
        }
        binding.btnDeleteText.setOnClickListener {
            removeData()
        }
        binding.postBtn.setOnClickListener {
            createPost()
        }
    }

    private fun createPost() {
        val text = binding.startThread.text.toString()
        viewModel.createPost(
            requireContext(),
            text,
            selectedCommentOption,
            onSuccess = {
                showToast("Thread posted successfully!")
                findNavController().navigate(R.id.profileFragment)
            },
            onError = {
                showToast("Failed to post the thread. Please try again.")
            }
        )
    }

    private fun showUserData() {
        userInfoViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.username.setText(it.username)
                profile.photo.let { photoUrl ->
                    if (it.photo.isNullOrEmpty()) {
                        Glide.with(this).load(R.drawable.profile_photo).into(binding.photoAnswerTo)
                    } else {
                        val photoUrl = it.photo
                        Glide.with(this).load(photoUrl).circleCrop().into(binding.photoAnswerTo)
                    }
                }
            }
        }
    }

    private fun setupCharacterCount() {
        binding.startThread.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) >= 300) {
                    binding.startThread.setTextColor(Color.RED)
                    binding.postBtn.isEnabled = false
                } else {
                    binding.startThread.setTextColor(Color.BLACK)
                    binding.postBtn.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun chooseMedia() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST
        )
    }

    private fun removeData() {
        selectedImageUri = null
        binding.apply {
            imageView10.visibility = View.GONE
            btnDeleteText.visibility = View.GONE
            profilePhotoThread.visibility = View.GONE
            addThread.visibility = View.GONE
            addMedia.visibility = View.VISIBLE
        }

        binding.startThread.text.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedUri = data.data

            if (selectedUri.toString().contains("image")) {
                selectedImageUri = selectedUri
                selectedVideoUri = null
                Utils.selectedImageUri = selectedImageUri
                showSelectedImage(selectedImageUri)
            }
        }
    }

    private fun showSelectedImage(imageUri: Uri?) {
        binding.apply {
            imageView10.visibility = View.VISIBLE
            btnDeleteText.visibility = View.VISIBLE
            profilePhotoThread.visibility = View.VISIBLE
            addThread.visibility = View.VISIBLE
            addMedia.visibility = View.GONE
        }

        Glide.with(this)
            .load(imageUri)
            .override(300, 350)
            .into(binding.imageView10)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.popup_menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.optionAnyone -> {
                    selectedCommentOption = "anyone"
                    true
                }
                R.id.optionProfilesFollow -> {
                    selectedCommentOption = "profiles you follow"
                    binding.textReply.text = "Profiles You Follow"
                    true
                }
                R.id.optionMentionedOnly -> {
                    selectedCommentOption = "mentioned only"
                    binding.textReply.text = "Mentioned Only"
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}