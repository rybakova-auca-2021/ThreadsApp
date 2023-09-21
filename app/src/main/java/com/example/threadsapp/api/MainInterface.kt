package com.example.threadsapp.api

import com.example.threadsapp.model.HomeModel.PostModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface MainInterface {
    @GET("feed/following/")
    fun getFollowingPosts(@Header("Authorization") token: String): Call<PostModel>

    @GET("feed/for_you/")
    fun getForYouPosts(@Header("Authorization") token: String): Call<PostModel>
}