package com.example.threadsapp.model.ProfileModel

data class Profile(
    val pk: Int,
    val username: String,
    val full_name: String,
    val bio: String,
    val website: String,
    var photo: String?,
    val is_private: Boolean,
    val is_followed: String,
    val followers_count: String
)
