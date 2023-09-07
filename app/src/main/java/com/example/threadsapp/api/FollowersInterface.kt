package com.example.threadsapp.api

import com.example.threadsapp.model.ProfileModel.Followee
import com.example.threadsapp.model.ProfileModel.FollowerApi
import com.example.threadsapp.model.ProfileModel.FollowerList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowersInterface {
    @POST("user/profile/unfollow/")
    fun unfollow(@Body request: Followee): Call<Followee>

    @POST("user/profile/follow/")
    fun follow(@Body request: Followee): Call<Followee>

    @POST("user/profile/mutual_follow/")
    fun mutualFollow(@Body request: Followee): Call<Followee>

    @POST("user/profile/delete/")
    fun deleteFollower(@Body request: FollowerApi): Call<FollowerApi>

    @POST("user/profile/follow_requests/allow/")
    fun allowRequest(@Body request: FollowerApi): Call<FollowerApi>

    @POST("user/profile/follow_requests/decline/")
    fun declineRequest(@Body request: FollowerApi): Call<FollowerApi>

    @GET("user/profile/followers/{followee_pk}/")
    fun readFollowers(@Path("followee_pk") followee_pk: Int): Call<FollowerList>

    @POST("user/profile/followers/{follower_pk}/")
    fun readFollows(@Path("followee_pk") follower_pk: Int): Call<FollowerList>

    @POST("user/profile/follow_requests/")
    fun readFollowRequests(@Body request: FollowerList): Call<FollowerList>
}