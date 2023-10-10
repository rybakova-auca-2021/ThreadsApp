package com.example.threadsapp.viewModel.followViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.FollowResponse
import com.example.threadsapp.model.ProfileModel.FollowerApi
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestViewModel : ViewModel() {
    fun allowRequest(
        id: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val request = FollowerApi(id)
        val apiInterface = RetrofitInstance.followApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.allowRequest(authHeader, request)
        call.enqueue(object : Callback<FollowResponse> {
            override fun onResponse(call: Call<FollowResponse>, response: Response<FollowResponse>) {
                if (response.isSuccessful) {
                    println("request has been allowed")
                } else {
                    println("failed to allow request")
                }
            }

            override fun onFailure(call: Call<FollowResponse>, t: Throwable) {
                println("network error")
            }
        })
    }

    fun declineRequest(
        id: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val request = FollowerApi(id)
        val apiInterface = RetrofitInstance.followApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.declineRequest(authHeader, request)
        call.enqueue(object : Callback<FollowerApi> {
            override fun onResponse(call: Call<FollowerApi>, response: Response<FollowerApi>) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onError.invoke("fail to decline request")
                }
            }

            override fun onFailure(call: Call<FollowerApi>, t: Throwable) {
                onError.invoke("Network error")
            }
        })
    }
}