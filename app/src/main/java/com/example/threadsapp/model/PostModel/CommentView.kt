package com.example.threadsapp.model.PostModel

data class Links(
    val next: Int,
    val previous: Int
)
data class ReplyView(
    val id: Int,
    val author: Int,
    val text: String,
    val date_posted: String,
)

data class CommentView(
    val id: Int,
    val post: Int,
    val author: Int,
    val text: String,
    val date_posted: String,
    val reply: ReplyView,
    var total_likes: String,
    val user_like: Boolean,
)

data class CommentResponse(
    val links: Links,
    val count: Int,
    val results: List<CommentView>
)



