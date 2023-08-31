package com.example.threadsapp.model.ProfileModel

data class UpdateRequest(
    val username: String,
    val bio: String,
    val website: String,
    val location: String,
)
