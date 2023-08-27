package com.example.threadsapp.model.AuthModel

data class ForgotPassword(
    val email: String
)

data class ForgotPasswordUpdate(
    val email: String,
    val otp: Int,
    val password: String,
    val password2: String
)

data class ForgotPasswordVerify(
    val email: String,
    val otp: Int,
)
