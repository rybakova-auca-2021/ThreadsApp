package com.example.threadsapp.model.HomeModel

data class Notification(
    val pk: Int,
    val owner: Int,
    val text: String,
    val related_user: Int?,
    val related_post: Int?,
    val related_comment: Int?,
    val date_posted: String
)