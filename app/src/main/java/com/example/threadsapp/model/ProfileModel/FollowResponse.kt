package com.example.threadsapp.model.ProfileModel

data class FollowResponse(
    val followee: UserProfileData,
    val follower: UserProfileData,
    val allowed: Boolean
)

