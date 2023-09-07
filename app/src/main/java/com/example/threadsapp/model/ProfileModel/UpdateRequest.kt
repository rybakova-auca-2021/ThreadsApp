package com.example.threadsapp.model.ProfileModel

data class UpdateRequest(
    val username: String,
    val full_name: String,
    val bio: String,
    val website: String,
)
