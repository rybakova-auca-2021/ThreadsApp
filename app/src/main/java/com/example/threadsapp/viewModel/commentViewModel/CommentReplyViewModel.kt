package com.example.threadsapp.viewModel.commentViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.HomeModel.Comment
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentReplyViewModel() : ViewModel() {
    fun createCommentReply(
        id: Int,
        text: String
    ) {
        val apiInterface = RetrofitInstance.postApi
        val request = Comment(text)

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.commentReplyCreate(authHeader, request, id.toString())
        call.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    println("comment reply has been created")
                } else {
                    println("there was an error to create a comment reply")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
            }
        })
    }
}