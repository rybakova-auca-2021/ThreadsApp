package com.example.threadsapp.api

import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.ProfileModel.Profile
import com.example.threadsapp.model.ProfileModel.UpdateRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileInterface {
    @GET("user/profile/me/")
    fun getUserInfo(): Call<Profile>

    @PATCH("user/profile/update/")
    fun partialUpdate(@Body request: UpdateRequest): Call<Profile>

    @PUT("user/profile/update/")
    fun update(@Body request: UpdateRequest): Call<Profile>

    @Multipart
    @PATCH("user/profile/me/edit_photo/")
    fun editPhoto(@Part photo: MultipartBody.Part): Call<DetailResponse>

    @GET("user/profile/{username}")
    fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ) : Call<Profile>

    @GET("user/profile/{id}")
    fun getUserProfileById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ) : Call<Profile>
}