package com.example.threadsapp.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentReplyBinding

class ReplyFragment : Fragment() {
    private lateinit var binding:FragmentReplyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReplyBinding.inflate(inflater, container, false)
        return binding.root
    }
}