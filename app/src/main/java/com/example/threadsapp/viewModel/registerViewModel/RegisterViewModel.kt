package com.example.threadsapp.viewModel.registerViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.ConfirmEmail
import com.example.threadsapp.model.AuthModel.ConfirmEmailUpdate
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.AuthModel.LoginResponse
import com.example.threadsapp.model.AuthModel.SignUp
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    fun register(
        username: String,
        email: String,
        password: String,
        passwordRepeat: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val request = SignUp(username, email, password, passwordRepeat)
        val apiInterface = RetrofitInstance.authApi

        val call = apiInterface.signUp(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val response = response.body()
                    Utils.token = response?.access
                    onSuccess.invoke()
                } else {
                    onError.invoke()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }

    fun sendEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val request = ConfirmEmail(email)
        val apiInterface = RetrofitInstance.authApi

        val call = apiInterface.confirmEmail(request)
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
        val request = ConfirmEmailUpdate(email, otp)
        val apiInterface = RetrofitInstance.authApi

        val call = apiInterface.confirmEmailUpdate(request)
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