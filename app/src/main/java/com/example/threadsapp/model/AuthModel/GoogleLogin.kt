package com.example.threadsapp.model.AuthModel

data class GoogleLogin(
    val access_token: String,
    val code: String,
    val id_token: String
)
