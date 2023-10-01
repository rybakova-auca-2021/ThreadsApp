package com.example.threadsapp.viewModel.followViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.FollowResponse
import com.example.threadsapp.model.ProfileModel.Followee
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowSomeoneViewModel : ViewModel() {
    fun follow(
        id: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val request = Followee(id)
        val apiInterface = RetrofitInstance.followApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.follow(authHeader, request)
        call.enqueue(object : Callback<FollowResponse> {
            override fun onResponse(call: Call<FollowResponse>, response: Response<FollowResponse>) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onError.invoke("fail to follow")
                }
            }

            override fun onFailure(call: Call<FollowResponse>, t: Throwable) {
                onError.invoke("Network error")
            }
        })
    }
}