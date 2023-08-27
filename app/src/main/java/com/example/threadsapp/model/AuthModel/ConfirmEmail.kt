package com.example.threadsapp.model.AuthModel

data class ConfirmEmail(
    val email: String
)

data class ConfirmEmailUpdate(
    val email: String,
    val otp: Int
)