package com.example.threadsapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.threadsapp.databinding.FragmentFollowing2Binding

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowing2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowing2Binding.inflate(inflater, container, false)
        return binding.root
    }
}