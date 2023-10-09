package com.example.threadsapp.api

import com.example.threadsapp.model.HomeModel.NotificationResponse
import com.example.threadsapp.model.HomeModel.PostModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MainInterface {
    @GET("feed/following/")
    fun getFollowingPosts(@Header("Authorization") token: String): Call<PostModel>

    @GET("feed/for_you/")
    fun getForYouPosts(@Header("Authorization") token: String): Call<PostModel>

    @GET("notifications/")
    fun getNotifications(@Header("Authorization") token: String): Call<NotificationResponse>

    @GET("notifications/{type}/")
    fun getNotificationsByType(
        @Header("Authorization") token: String,
        @Path("type") type: String
    ): Call<NotificationResponse>
}