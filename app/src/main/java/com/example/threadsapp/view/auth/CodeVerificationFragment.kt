package com.example.threadsapp.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentCodeVerificationBinding

class CodeVerificationFragment : Fragment() {
    private lateinit var binding: FragmentCodeVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCodeVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_codeVerificationFragment_to_resetPasswordFragment)
        }
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_codeVerificationFragment_to_createPasswordFragment)
        }
        binding.sendAgainBtn.setOnClickListener {
            // TODO
        }
    }
}