package com.example.threadsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThreadData(
    val profilePhoto: Int,
    val username: String,
    val text: String,
    val time: String,
    val replies: String,
    val likes: String,
    val images: Int
) : Parcelable
