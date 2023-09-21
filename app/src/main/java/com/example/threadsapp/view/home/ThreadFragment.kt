package com.example.threadsapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.threadsapp.ProfileActivity
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentThreadBinding
import com.example.threadsapp.model.ThreadData
import com.google.android.material.bottomnavigation.BottomNavigationView

class ThreadFragment : Fragment() {
    private lateinit var binding:FragmentThreadBinding

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
        val clickedPost = arguments?.getParcelable<ThreadData>("threads")
        clickedPost?.let { post ->
            binding.avatar.setImageResource(post.profilePhoto)
            binding.username.text = post.username
            binding.threadText.text = post.text
            binding.time.text = post.time
            binding.replies.text = post.replies
            binding.likes.text = post.likes
            binding.imageView4.setImageResource(post.images)
        }
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }
    }
}