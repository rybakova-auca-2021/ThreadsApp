package com.example.threadsapp.model.ProfileModel

data class UserProfileData(
    val pk: Int,
    val username: String,
    val full_name: String,
    val bio: String,
    val website: String,
    val location: String,
    val photo: String
)

data class Followers(
    val follower: UserProfileData,
    val is_followed: String
)

data class Follows(
    val followee: UserProfileData,
    val is_followed: String
)

data class PaginationLinks(
    val next: Int?,
    val previous: Int?
)

data class FollowerResult(
    val links: PaginationLinks,
    val count: Int,
    val results: List<Followers>
)

data class FollowsResult(
    val links: PaginationLinks,
    val count: Int,
    val results: List<Follows>
)
