package com.example.threadsapp.viewModel.postViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeUnlikeViewModel : ViewModel() {
    fun likeOrUnlike(
        id: Int
    ) {
        val apiInterface = RetrofitInstance.postApi
        val token = Utils.token
        val authHeader = "Bearer $token"

        if (token != null) {
            apiInterface.likeOrUnlike(authHeader, id.toString()).enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful) {
                        println(response.body()?.toString())
                    } else {
                        println(response.errorBody()?.string())
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    println(t.message)
                }
            })
        }
    }
}