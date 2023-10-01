package com.example.threadsapp.viewModel.followViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.Followee
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnfollowViewModel : ViewModel() {
    fun unfollow(
        id: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val request = Followee(id)
        val apiInterface = RetrofitInstance.followApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.unfollow(authHeader, request)
        call.enqueue(object : Callback<Followee> {
            override fun onResponse(call: Call<Followee>, response: Response<Followee>) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onError.invoke("fail to unfollow")
                }
            }

            override fun onFailure(call: Call<Followee>, t: Throwable) {
                onError.invoke("Network error")
            }
        })
    }
}