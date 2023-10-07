package com.example.threadsapp.model.HomeModel

data class Links(
    val next: Int,
    val previous: Int
)

data class RepostView(
    val id: Int?,
    val author: Int,
    val text: String?,
    val image: String?,
    val video: String?,
    val repost: Int?,
    val date_posted: String?
)

data class PostView(
    val id: Int,
    val author: Int,
    val text: String?,
    val date_posted: String,
    val image: String?,
    val video: String?,
    val repost: RepostView?,
    val comments_permission: String,
    val total_comments: Int,
    var total_likes: Int,
    val user_like: Boolean
)

data class PostModel(
    val links: Links,
    val count: Int,
    val results: List<PostView>
)