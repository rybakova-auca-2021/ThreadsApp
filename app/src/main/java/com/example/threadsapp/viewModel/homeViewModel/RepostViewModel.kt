package com.example.threadsapp.viewModel.homeViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepostViewModel : ViewModel() {
    fun repost(
        id: Int
    ) {
        val apiInterface = RetrofitInstance.postApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.createRepost(authHeader, id.toString())
        call.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    println("repost has been created")
                } else {
                    println("there was an error to create a repost")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
            }
        })
    }
}