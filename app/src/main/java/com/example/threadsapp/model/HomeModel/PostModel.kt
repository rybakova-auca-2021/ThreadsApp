package com.example.threadsapp.model.HomeModel

data class Links(
    val next: Int,
    val previous: Int
)

data class RepostView(
    val id: Int,
    val author: Int,
    val text: String,
    val image: String,
    val video: String,
    val repost: String,
    val date_posted: String
)

data class PostView(
    val id: Int,
    val title: String,
    val author: Int,
    val text: String?,
    val date_posted: String,
    val image: String?,
    val video: String?,
    val repost: RepostView,
    val comments_permission: String,
    val total_likes: String,
    val user_like: String
)

data class PostModel(
    val links: Links,
    val count: Int,
    val results: List<PostView>
)
