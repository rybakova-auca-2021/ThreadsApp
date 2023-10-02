package com.example.threadsapp.view.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentReplyBinding
import com.example.threadsapp.model.ThreadData
import com.example.threadsapp.util.CalculateTime
import com.example.threadsapp.util.ImageUtil
import com.example.threadsapp.viewModel.commentViewModel.CreateCommentViewModel
import com.example.threadsapp.viewModel.postViewModel.PostDataViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReplyFragment : Fragment() {
    private lateinit var binding:FragmentReplyBinding
    private val viewModel: PostDataViewModel by viewModels()
    private val userProfileViewModel: SomeoneProfileViewModel by viewModels()
    private val myDataViewModel: UserInfoViewModel by viewModels()
    private val commentCreateViewModel: CreateCommentViewModel by viewModels()
    private var PICK_IMAGE_REQUEST  = 1
    private var selectedImageUri: Uri? = null
    private var postId: Int? = null
    private val args: ReplyFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReplyBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setPostData()
        setMyData()
        myDataViewModel.getInfo()
    }

    private fun createComment(id: Int) {
        commentCreateViewModel.createComment(
            id,
            binding.replyTo.text.toString(),
            onSuccess = {
                findNavController().navigate(R.id.homeFragment)
            }
        )
    }

    private fun setPostData() {
        viewModel.readData(
            args.postId,
            onSuccess = { post ->
                userProfileViewModel.getUserProfileById(
                    post.author,
                    onSuccess = { userProfile ->
                        binding.username.text = userProfile.username
                        userProfile.photo.let { photoUrl ->
                            if (photoUrl.isNullOrEmpty()) {
                                Glide.with(binding.root.context).load(R.drawable.profile_photo)
                                    .into(binding.photoAnswerTo)
                            } else {
                                Glide.with(binding.root.context).load(photoUrl).circleCrop()
                                    .into(binding.photoAnswerTo)
                            }
                        }

                        if (post.text != null) {
                            binding.text.text = post.text
                        } else {
                            binding.text.visibility = View.GONE
                        }
                        if (post.image != null) {
                            Glide.with(binding.imageHolder)
                                .load(post.image)
                                .into(binding.imageHolder)
                            binding.imageHolder.visibility = View.VISIBLE
                        } else {
                            binding.imageHolder.visibility = View.GONE
                        }
                    },
                    onError = { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onError = {
                Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setMyData() {
        myDataViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.myUsername.setText(it.username)
                profile.photo.let { photoUrl ->
                    if (it.photo.isNullOrEmpty()) {
                        Glide.with(this).load(R.drawable.profile_photo).into(binding.myPhoto)
                    } else {
                        val photoUrl = it.photo
                        Glide.with(this).load(photoUrl).circleCrop().into(binding.myPhoto)
                    }
                }
            }
        }
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.addMedia.setOnClickListener {
            addPhoto()
        }
        binding.postBtn.setOnClickListener {
            createComment(args.postId)
        }
    }

    private fun addPhoto() {
        ImageUtil.chooseImage(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let { ImageUtil.loadImage(this, binding.imageView2, it) }
        } else {
            binding.imageView2.isVisible = false
        }
    }
}