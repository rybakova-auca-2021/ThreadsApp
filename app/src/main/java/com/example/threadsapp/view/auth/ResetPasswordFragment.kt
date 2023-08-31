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
import com.example.threadsapp.databinding.FragmentResetPasswordBinding
import com.example.threadsapp.viewModel.forgotPasswordViewModel.ForgotPasswordViewModel

class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
        binding.btnContinue.setOnClickListener {
            handleSendingEmail()
        }
    }

    private fun handleSendingEmail() {
        val email = binding.btnEmail.text.toString()

        viewModel.sendEmail(
            email,
            onSuccess = {
                saveEmailToSharedPreferences(email)
                navigateToOtp()
                showToast("Email was sent")
            },
            onError = {
                showToast("Please try again.")
            }
        )
    }

    private fun saveEmailToSharedPreferences(email: String) {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }


    private fun navigateToOtp() {
        findNavController().navigate(R.id.action_resetPasswordFragment_to_codeVerificationFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}