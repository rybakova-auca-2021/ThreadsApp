package com.example.threadsapp.view.auth

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.threadsapp.ProfileActivity
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentLoginBinding
import com.example.threadsapp.viewModel.loginViewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setupNavigation()
        setupClickListeners()

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun setupNavigation() {
        binding.loginButton.setOnClickListener {
            handleLogin()
        }
        binding.btnForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.signInGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
        val task: Task<GoogleSignInAccount> =
            GoogleSignIn.getSignedInAccountFromIntent(data)
        handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val token = completedTask.result.idToken
            Log.d(TAG, "sign im is successful. token is $token")

        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun handleLogin() {
        val email = binding.emailButton.text.toString()
        val password = binding.passwordButton.text.toString()

        viewModel.login(
            email, password,
            onSuccess = { navigateToProfile() },
            onError = { showNotification() }
        )
    }

    private fun navigateToProfile() {
        val profileIntent = Intent(requireContext(), ProfileActivity::class.java)
        startActivity(profileIntent)
    }

    private fun setupClickListeners() {
        binding.eye.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun togglePasswordVisibility() {
        val isPasswordVisible =
            binding.passwordButton.transformationMethod == HideReturnsTransformationMethod.getInstance()
        val newTransformationMethod =
            if (isPasswordVisible) PasswordTransformationMethod.getInstance() else HideReturnsTransformationMethod.getInstance()
        binding.passwordButton.transformationMethod = newTransformationMethod
        binding.passwordButton.setSelection(binding.passwordButton.text?.length ?: 0)
    }

    private fun showDialog(layoutResId: Int, vararg clickableViews: Int, onClickAction: () -> Unit) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(layoutResId, null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogView)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        clickableViews.forEach { viewId ->
            dialogView.findViewById<View>(viewId)?.setOnClickListener {
                myDialog.dismiss()
                onClickAction.invoke()
            }
        }
    }

    private fun showNotification() {
        showDialog(R.layout.dialog_incorrect_username, R.id.btnTryAgain) {}
    }

    private fun showForgotPasswordDialog() {
        showDialog(R.layout.dialog_forgot_password, R.id.btnSend, R.id.btnTryAgain) {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }
}
