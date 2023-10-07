package com.example.threadsapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentQuotedThreadBinding
import com.example.threadsapp.viewModel.homeViewModel.QuoteViewModel
import com.example.threadsapp.viewModel.postViewModel.PostDataViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class QuotedThreadFragment : Fragment() {
    private lateinit var binding: FragmentQuotedThreadBinding
    private val args: QuotedThreadFragmentArgs by navArgs()
    private val userProfileViewModel: SomeoneProfileViewModel by viewModels()
    private val myDataViewModel: UserInfoViewModel by viewModels()
    private val quoteCreate: QuoteViewModel by viewModels()
    private val postInfoViewModel: PostDataViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuotedThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setPostData()
        setMyData()
        myDataViewModel.getInfo()
    }


    private fun setupNavigation() {
        binding.postBtn.setOnClickListener {
            setupQuote()
        }
    }

    private fun setPostData() {
        postInfoViewModel.readData(
            args.postId,
            onSuccess = { post ->
                userProfileViewModel.getUserProfileById(
                    post.author,
                    onSuccess = { userProfile ->
                        binding.usernameThread.text = userProfile.username
                        userProfile.photo.let { photoUrl ->
                            if (photoUrl.isNullOrEmpty()) {
                                Glide.with(binding.root.context).load(R.drawable.profile_photo)
                                    .into(binding.profilePhotoThread3)
                            } else {
                                Glide.with(binding.root.context).load(photoUrl).circleCrop()
                                    .into(binding.profilePhotoThread3)
                            }
                        }

                        if (post.text != null) {
                            binding.textThread.text = post.text
                        } else {
                            binding.textThread.visibility = View.GONE
                        }
                        if (post.image != null) {
                            Glide.with(binding.imageHolder)
                                .load(post.image)
                                .into(binding.imageHolder)
                            binding.imageHolder.visibility = View.VISIBLE
                        } else {
                            binding.imageHolder.visibility = View.GONE
                        }
                        binding.likes.text = "${post.total_likes} likes"
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

    private fun setupQuote() {
        quoteCreate.quoteThread(
            args.postId,
            binding.startThread.text.toString(),
            onSuccess = {
                findNavController().navigate(R.id.profileFragment)
            }
        )
    }

}