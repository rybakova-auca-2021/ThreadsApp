package com.example.threadsapp.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.threadsapp.ProfileActivity
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentThreadBinding
import com.example.threadsapp.model.ThreadData
import com.example.threadsapp.util.CalculateTime
import com.example.threadsapp.viewModel.postViewModel.PostDataViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ThreadFragment : Fragment() {
    private lateinit var binding:FragmentThreadBinding
    private val viewModel: PostDataViewModel by viewModels()
    private val userProfileViewModel: SomeoneProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as ProfileActivity).hideBtmNav()
        setData()
        setupNavigation()
    }

    private fun setData() {
        val clickedPostId = arguments?.getInt("postId")

        if (clickedPostId != null) {
            viewModel.readData(
                clickedPostId,
                onSuccess = { post ->
                    userProfileViewModel.getUserProfileById(
                        post.author,
                        onSuccess = { userProfile ->
                            binding.username.text = userProfile.username
                            userProfile.photo.let { photoUrl ->
                                if (photoUrl.isNullOrEmpty()) {
                                    Glide.with(binding.root.context).load(R.drawable.profile_photo)
                                        .into(binding.avatar)
                                } else {
                                    Glide.with(binding.root.context).load(photoUrl).circleCrop()
                                        .into(binding.avatar)
                                }
                            }

                            if (post.text != null) {
                                binding.threadText.text = post.text
                            } else {
                                binding.threadText.visibility = View.GONE
                            }
                            val timeDifference =
                                CalculateTime.calculateTimeDifference(post.date_posted)
                            binding.time.text = timeDifference
                            binding.likes.text = "${post.total_likes} likes"
                            if (post.image != null) {
                                Glide.with(binding.imageView4)
                                    .load(post.image)
                                    .into(binding.imageView4)
                            } else {
                                binding.imageView4.isVisible = false
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
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }
    }
}