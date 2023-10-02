package com.example.threadsapp.viewModel.commentViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeUnlikeCommentViewModel : ViewModel() {
    fun likeComment(
        text: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val apiInterface = RetrofitInstance.postApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.likeOrUnlikeComment(authHeader, text)
        call.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onError.invoke()
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }
}