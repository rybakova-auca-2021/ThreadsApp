package com.example.threadsapp.model.AuthModel

data class Login(
    val email: String,
    val password: String
)

data class LoginResponse(
    val access: String
)