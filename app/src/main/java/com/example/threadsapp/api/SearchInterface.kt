package com.example.threadsapp.api

import com.example.threadsapp.model.ProfileModel.SearchHashtagResult
import com.example.threadsapp.model.SearchUserResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SearchInterface {
    @GET("search/users/{search_obj}/")
    fun searchUsers(
        @Header("Authorization") token: String,
        @Path("search_obj") search_obj: String
    ): Call<SearchUserResult>

    @GET("search/hashtag/{search_obj}/")
    fun searchHashtag(
        @Header("Authorization") token: String,
        @Path("search_obj") search_obj: String
    ): Call<List<SearchHashtagResult>>
}