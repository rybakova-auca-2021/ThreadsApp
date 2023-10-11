package com.example.threadsapp.view.registration

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
import com.example.threadsapp.databinding.FragmentSignUpBinding
import com.example.threadsapp.viewModel.registerViewModel.RegisterViewModel

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupClickListeners()
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

    private fun setupNavigation() {
        binding.btnCreate.setOnClickListener {
            handleRegistration()
        }
        binding.btnBack.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun setupClickListeners() {
        binding.passwordEye.setOnClickListener {
            togglePasswordVisibility()
        }
        binding.passwordEye2.setOnClickListener {
            togglePasswordRepeatVisibility()
        }
    }

    private fun handleRegistration() {
        val email = binding.btnEmail.text.toString()
        val username = binding.btnName.text.toString()
        val password = binding.btnPassword.text.toString()
        val passwordRepeat = binding.btnPasswordRepeat.text.toString()

        if (areFieldsValid(email, username, password, passwordRepeat)) {
            viewModel.register(
                email, username, password, passwordRepeat,
                onSuccess = {
                    handleSendingEmail()
                    saveEmailToSharedPreferences(email, username)
                },
                onError = {
                    showToast("Registration failed. Please try again.")
                }
            )
        }
    }

    private fun handleSendingEmail() {
        val email = binding.btnEmail.text.toString()

        viewModel.sendEmail(
            email,
            onSuccess = {
                navigateToLogin()
            },
            onError = {
                showToast("Please try again.")
            }
        )
    }

    private fun saveEmailToSharedPreferences(email: String, username: String) {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("username", username)
        editor.apply()
    }


    private fun areFieldsValid(
        email: String,
        username: String,
        password: String,
        passwordRepeat: String
    ): Boolean {
        return email.isNotEmpty() &&
                username.isNotEmpty() &&
                password.isNotEmpty() &&
                passwordRepeat.isNotEmpty() &&
                password == passwordRepeat
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_signUpFragment_to_confirmEmailFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}