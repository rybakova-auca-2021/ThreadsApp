package com.example.threadsapp.viewModel.loginViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.Login
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
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onError.invoke()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onError.invoke()
            }
        })
    }
}