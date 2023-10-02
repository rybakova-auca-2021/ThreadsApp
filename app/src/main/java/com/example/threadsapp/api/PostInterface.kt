package com.example.threadsapp.api

import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.HomeModel.PostModel
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.model.PostModel.Comment
import com.example.threadsapp.model.PostModel.CommentResponse
import com.example.threadsapp.model.PostModel.Quote
import com.example.threadsapp.model.PostModel.Reply
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PostInterface {
    @GET("post/")
    fun getPosts(@Header("Authorization") token: String): Call<PostModel>

    @Multipart
    @POST("post/")
    fun createPost(
        @Header("Authorization") token: String,
        @Part("text") text: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part video: MultipartBody.Part?,
        @Part("comments_permission") comments_permission: RequestBody,
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

    @GET("post/{post_id}/view/")
    fun postRead(
        @Header("Authorization") token: String,
        @Path("post_id") id: String
    ): Call<PostView>

    @DELETE("post/{id}/")
    fun postDelete(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<Unit>

    @GET("post/{post_id}/comments/")
    fun postCommentsList(
        @Header("Authorization") token: String,
        @Path("post_id") id: String
    ): Call<CommentResponse>

    @POST("post/{post_id}/comments/")
    fun createComment(
        @Header("Authorization") token: String,
        @Body request: Comment,
        @Path("post_id") id: String
    ): Call<DetailResponse>

    @DELETE("post/comment/comment_id/")
    fun deleteComment(
        @Header("Authorization") token: String,
        @Path("comment_id") id: String
    ): Call<DetailResponse>

    @PATCH("post/comments/{comment_id}/like_unlike/")
    fun likeOrUnlikeComment(
        @Header("Authorization") token: String,
        @Path("comment_id") id: String
    ): Call<DetailResponse>

    @POST("post/comments/{comment_id}/reply/")
    fun commentReplyCreate(
        @Header("Authorization") token: String,
        @Body request: Comment,
        @Path("comment_id") id: String
    ): Call<DetailResponse>

    @POST("post/{post_id}/quote/")
    fun createQuote(
        @Header("Authorization") token: String,
        @Body request: Quote,
        @Path("post_id") id: String
    ): Call<DetailResponse>

    @POST("post/{post_id}/repost/")
    fun createRepost(
        @Header("Authorization") token: String,
        @Path("post_id") id: String
    ): Call<DetailResponse>
}