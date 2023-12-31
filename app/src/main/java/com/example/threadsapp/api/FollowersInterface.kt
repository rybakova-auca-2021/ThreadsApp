package com.example.threadsapp.api

import com.example.threadsapp.model.ProfileModel.FollowResponse
import com.example.threadsapp.model.ProfileModel.Followee
import com.example.threadsapp.model.ProfileModel.FollowerApi
import com.example.threadsapp.model.ProfileModel.FollowerList
import com.example.threadsapp.model.ProfileModel.FollowerResult
import com.example.threadsapp.model.ProfileModel.FollowsResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowersInterface {
    @POST("user/profile/unfollow/")
    fun unfollow(
        @Header("Authorization") token: String,
        @Body request: Followee
    ): Call<Followee>

    @POST("user/profile/follow/")
    fun follow(
        @Header("Authorization") token: String,
        @Body request: Followee
    ): Call<FollowResponse>

    @POST("user/profile/mutual_follow/")
    fun mutualFollow(
        @Header("Authorization") token: String,
        @Body request: Followee
    ): Call<Followee>

    @POST("user/profile/delete/")
    fun deleteFollower(@Body request: FollowerApi): Call<FollowerApi>

    @POST("user/profile/follow_requests/allow/")
    fun allowRequest(
        @Header("Authorization") token: String,
        @Body request: FollowerApi
    ): Call<FollowResponse>

    @POST("user/profile/follow_requests/decline/")
    fun declineRequest(
        @Header("Authorization") token: String,
        @Body request: FollowerApi
    ): Call<FollowerApi>

    @GET("user/profile/followers/{followee_pk}/")
    fun readFollowers(
        @Header("Authorization") token: String,
        @Path("followee_pk") followee_pk: Int
    ): Call<FollowerResult>

    @GET("user/profile/follows/{follower_pk}/")
    fun readFollows(
        @Header("Authorization") token: String,
        @Path("follower_pk") follower_pk: Int
    ): Call<FollowsResult>

    @GET("user/profile/follow_requests/")
    fun readFollowRequests(@Header("Authorization") token: String): Call<FollowerResult>
}