package com.example.threadsapp.model

data class Follower(
    val username: String,
    val name: String,
    var isFollowing: Boolean,
    var isRequested: Boolean
)
