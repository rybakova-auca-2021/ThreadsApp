package com.example.threadsapp.viewModel.forgotPasswordViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.AuthModel.ForgotPassword
import com.example.threadsapp.model.AuthModel.ForgotPasswordUpdate
import com.example.threadsapp.model.AuthModel.ForgotPasswordVerify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel: ViewModel() {
    fun sendEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val request = ForgotPassword(email)
        val apiInterface = RetrofitInstance.authApi

        val call = apiInterface.forgotPassword(request)
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

    fun verifyOtp(
        email: String,
        otp: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val request = ForgotPasswordVerify(email, otp)
        val apiInterface = RetrofitInstance.authApi

        val call = apiInterface.forgotPasswordVerify(request)
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