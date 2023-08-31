package com.example.threadsapp.model.ProfileModel

data class Profile(
    val pk: Int,
    val username: String,
    val bio: String,
    val website: String,
    val location: String,
    val photo: String
)
