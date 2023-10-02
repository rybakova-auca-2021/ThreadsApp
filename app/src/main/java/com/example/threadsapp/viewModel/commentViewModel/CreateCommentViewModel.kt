package com.example.threadsapp.viewModel.commentViewModel

import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.PostModel.Comment
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateCommentViewModel {
    fun createComment(
        id: Int,
        text: String
    ) {
        val apiInterface = RetrofitInstance.postApi
        val request = Comment(text)

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.createComment(authHeader, request, id.toString())
        call.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    println("comment has been created")
                } else {
                    println("there was an error to create a comment")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
            }
        })
    }
}