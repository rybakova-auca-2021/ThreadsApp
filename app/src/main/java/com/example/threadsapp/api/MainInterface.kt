package com.example.threadsapp.api

import com.example.threadsapp.model.HomeModel.Notification
import com.example.threadsapp.model.HomeModel.PostModel
import com.example.threadsapp.model.HomeModel.PostView
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MainInterface {
    @GET("feed/following/")
    fun getFollowingPosts(@Header("Authorization") token: String): Call<PostModel>

    @GET("feed/for_you/")
    fun getForYouPosts(@Header("Authorization") token: String): Call<PostModel>

    @GET("post/{user_id}/list/")
    fun getUserPosts(
        @Header("Authorization") token: String,
        @Path("user_id") userId: Int
    ): Call<List<PostView>>

    @GET("notifications/")
    fun getNotifications(@Header("Authorization") token: String): Call<List<Notification>>

    @GET("notifications/{type}/")
    fun getNotificationsByType(
        @Header("Authorization") token: String,
        @Path("type") type: String
    ): Call<List<Notification>>
}