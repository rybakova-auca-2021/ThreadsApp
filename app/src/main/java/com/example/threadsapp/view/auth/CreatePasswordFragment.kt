package com.example.threadsapp.view.auth

import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentCreatePasswordBinding
import com.example.threadsapp.viewModel.forgotPasswordViewModel.ForgotPasswordViewModel

class CreatePasswordFragment : Fragment() {
    private lateinit var binding: FragmentCreatePasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupClickListeners()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_createPasswordFragment_to_codeVerificationFragment)
        }
        binding.btnCreate.setOnClickListener {
            handlePasswordCreation()
        }
    }

    private fun handlePasswordCreation() {
        val email = getEmailFromSharedPreferences()
        val otp = getOtpFromSharedPreferences()
        val password = binding.btnPassword.text.toString()
        val password2 = binding.btnPasswordRepeat.text.toString()

        viewModel.createPassword(
            email,
            otp.toInt(),
            password,
            password2,
            onSuccess = {
                navigateToLogin()
                showToast("New password was created")
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

    private fun getOtpFromSharedPreferences(): String {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("otp", "") ?: ""
    }

    private fun setupClickListeners() {
        binding.passwordEye.setOnClickListener {
            togglePasswordVisibility()
        }
        binding.passwordEye2.setOnClickListener {
            togglePasswordRepeatVisibility()
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_createPasswordFragment_to_loginFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun togglePasswordVisibility() {
        val isPasswordVisible = binding.btnPassword.transformationMethod == HideReturnsTransformationMethod.getInstance()
        val newTransformationMethod = if (isPasswordVisible) PasswordTransformationMethod.getInstance() else HideReturnsTransformationMethod.getInstance()
        binding.btnPassword.transformationMethod = newTransformationMethod
        binding.btnPassword.setSelection(binding.btnPassword.text?.length ?: 0)
    }

    private fun togglePasswordRepeatVisibility() {
        val isPasswordVisible2 = binding.btnPasswordRepeat.transformationMethod == HideReturnsTransformationMethod.getInstance()
        val newTransformationMethod2 = if (isPasswordVisible2) PasswordTransformationMethod.getInstance() else HideReturnsTransformationMethod.getInstance()
        binding.btnPasswordRepeat.transformationMethod = newTransformationMethod2
        binding.btnPasswordRepeat.setSelection(binding.btnPasswordRepeat.text?.length ?: 0)
    }
}