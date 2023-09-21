package com.example.threadsapp.viewModel.loginViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.Login
import com.example.threadsapp.model.AuthModel.LoginResponse
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    fun login(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val request = Login(username, password)
        val apiInterface = RetrofitInstance.authApi

        val call = apiInterface.login(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val response = response.body()
                    Utils.token = response?.access
                    Utils.refresh = response?.refresh
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
}