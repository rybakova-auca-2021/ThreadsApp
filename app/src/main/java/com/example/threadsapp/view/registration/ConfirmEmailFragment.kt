package com.example.threadsapp.view.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentConfirmEmailBinding
import com.example.threadsapp.viewModel.registerViewModel.RegisterViewModel

class ConfirmEmailFragment : Fragment() {
    private lateinit var binding: FragmentConfirmEmailBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_confirmEmailFragment_to_signUpFragment)
        }
        binding.btnContinue.setOnClickListener {
            handleCodeVerification()
        }
    }


    private fun handleCodeVerification() {
        val email = getEmailFromSharedPreferences()
        val otp = binding.pinView.text.toString()

        viewModel.verifyOtp(
            email,
            otp.toInt(),
            onSuccess = {
                navigateToOtp()
                showToast("Code was correct")
            },
            onError = {
                showToast("Please try again.")
            }
        )
    }

    private fun getEmailFromSharedPreferences(): String {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", "") ?: ""
    }

    private fun navigateToOtp() {
        findNavController().navigate(R.id.action_confirmEmailFragment_to_loginFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}