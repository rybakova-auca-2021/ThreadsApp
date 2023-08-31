package com.example.threadsapp.viewModel.registerViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.SignUp
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