package com.example.threadsapp.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentActivityFollowingBinding

class ActivityFollowingFragment : Fragment() {
    private lateinit var binding: FragmentActivityFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }
}