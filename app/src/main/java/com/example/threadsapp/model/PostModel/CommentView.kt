package com.example.threadsapp.model.PostModel

data class Links(
    val next: Int,
    val previous: Int
)
data class ReplyView(
    val id: Int,
    val author: Int,
    val text: String,
    val datePosted: String,
)

data class CommentView(
    val id: Int,
    val post: Int,
    val author: Int,
    val text: String,
    val datePosted: String,
    val reply: ReplyView,
    val totalLikes: String,
    val userLike: String,
)

data class CommentResponse(
    val links: Links,
    val count: Int,
    val results: List<CommentView>
)



