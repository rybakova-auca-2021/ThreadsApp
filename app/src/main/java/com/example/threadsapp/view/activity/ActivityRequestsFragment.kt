package com.example.threadsapp.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentActivityRequestsBinding

class ActivityRequestsFragment : Fragment() {
    private lateinit var binding: FragmentActivityRequestsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }
}