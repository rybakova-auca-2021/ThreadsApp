package com.example.threadsapp.api

import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.HomeModel.PostModel
import com.example.threadsapp.model.PostModel.Comment
import com.example.threadsapp.model.PostModel.Post
import com.example.threadsapp.model.PostModel.Quote
import com.example.threadsapp.model.PostModel.Reply
import com.example.threadsapp.model.PostModel.Repost
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface PostInterface {
    @GET("post/")
    fun getPosts(@Header("Authorization") token: String): Call<PostModel>

    @POST("post/")
    fun getPosts(
        @Header("Authorization") token: String,
        @Body request: Post
    ): Call<DetailResponse>

    @GET("post/by_hashtag/{tag_name}/")
    fun getPostByHashtag(
        @Header("Authorization") token: String,
        @Path("tag_name") tagName: String
    ): Call<PostModel>

    @POST("post/comments/{comment_id}/reply/")
    fun createReply(
        @Header("Authorization") token: String,
        @Body request: Reply,
        @Path("comment_id") commentId: String
    ): Call<DetailResponse>

    @PATCH("post/like_unlike/{post_id}/")
    fun likeOrUnlike(
        @Header("Authorization") token: String,
        @Path("post_id") postId: String
    ) : Call<DetailResponse>

    @GET("post/{id}")
    fun postRead(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<PostModel>

    @DELETE("post/{id}")
    fun postDelete(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<Unit>

    @GET("post/{post_id}/comments/")
    fun postCommentsList(
        @Header("Authorization") token: String,
        @Path("post_id") id: String
    ): Call<PostModel>

    @POST("post/{post_id}/comments/")
    fun createComment(
        @Header("Authorizatio") token: String,
        @Body request: Comment,
        @Path("post_id") id: String
    ): Call<Comment>

    @POST("post/{post_id}/quote/")
    fun createQuote(
        @Header("Authorization") token: String,
        @Body request: Quote,
        @Path("post_id") id: String
    ): Call<DetailResponse>

    @POST("post/{post_id}/repost/")
    fun createRepost(
        @Header("Authorization") token: String,
        @Body request: Repost,
        @Path("post_id") id: String
    ): Call<DetailResponse>
}