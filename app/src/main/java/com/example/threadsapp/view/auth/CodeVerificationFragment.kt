package com.example.threadsapp.view.auth

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
import com.example.threadsapp.databinding.FragmentCodeVerificationBinding
import com.example.threadsapp.viewModel.forgotPasswordViewModel.ForgotPasswordViewModel

class CodeVerificationFragment : Fragment() {
    private lateinit var binding: FragmentCodeVerificationBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

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
            handleCodeVerification()
        }
        binding.sendAgainBtn.setOnClickListener {
            // TODO
        }
    }

    private fun handleCodeVerification() {
        val email = getEmailFromSharedPreferences()
        val otp = binding.pinView.text.toString()

        viewModel.verifyOtp(
            email,
            otp.toInt(),
            onSuccess = {
                saveEmailAndOtpToSharedPreferences(email, otp)
                navigateToOtp()
                showToast("Code was correct")
            },
            onError = {
                showToast("Please try again.")
            }
        )
    }

    private fun saveEmailAndOtpToSharedPreferences(email: String, otp: String) {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("otp", otp)
        editor.apply()
    }

    private fun getEmailFromSharedPreferences(): String {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", "") ?: ""
    }

    private fun navigateToOtp() {
        findNavController().navigate(R.id.action_codeVerificationFragment_to_createPasswordFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}