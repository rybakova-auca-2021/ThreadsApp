package com.example.threadsapp.model

data class SearchUserResult(
    val links: Links,
    val count: Int,
    val results: List<UserResult>
)

data class Links(
    val next: String?,
    val previous: String?
)

data class UserResult(
    val pk: Int,
    val username: String,
    var is_followed: String
)
