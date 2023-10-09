package com.example.threadsapp.viewModel.profileViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.AuthModel.LogoutRequest
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutViewModel : ViewModel() {
    fun logout(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val apiInterface = RetrofitInstance.authApi

        val refresh = Utils.token
        val authHeader = "Bearer $refresh"

        val request = refresh?.let { LogoutRequest(it) }

        request?.let { apiInterface.logout(authHeader, it) }
            ?.enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
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