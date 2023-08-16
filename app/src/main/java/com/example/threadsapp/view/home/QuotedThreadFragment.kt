package com.example.threadsapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.threadsapp.databinding.FragmentQuotedThreadBinding

class QuotedThreadFragment : Fragment() {
    private lateinit var binding: FragmentQuotedThreadBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuotedThreadBinding.inflate(inflater, container, false)
        return binding.root
    }
}