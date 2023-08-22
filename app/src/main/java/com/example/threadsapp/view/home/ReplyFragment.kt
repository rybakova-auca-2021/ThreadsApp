package com.example.threadsapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentReplyBinding
import com.example.threadsapp.model.ThreadData
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReplyFragment : Fragment() {
    private lateinit var binding:FragmentReplyBinding

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
    }
}