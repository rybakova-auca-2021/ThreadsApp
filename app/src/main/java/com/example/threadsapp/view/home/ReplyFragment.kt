package com.example.threadsapp.view.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentReplyBinding
import com.example.threadsapp.model.ThreadData
import com.example.threadsapp.util.ImageUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReplyFragment : Fragment() {
    private lateinit var binding:FragmentReplyBinding
    private var PICK_IMAGE_REQUEST  = 1
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReplyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setupNavigation()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.GONE
    }

    private fun setData() {
        val clickedPost = arguments?.getParcelable<ThreadData>("threads")
        clickedPost?.let { post ->
            binding.profilePhotoThread.setImageResource(post.profilePhoto)
            binding.username.text = post.username
            binding.text.text = post.text
        }
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.addMedia.setOnClickListener {
            addPhoto()
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