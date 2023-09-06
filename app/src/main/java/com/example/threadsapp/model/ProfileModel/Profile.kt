package com.example.threadsapp.model.ProfileModel

data class Profile(
    val pk: Int,
    val username: String,
    val full_name: String,
    val bio: String,
    val website: String,
    val photo: String
)
