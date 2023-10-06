package com.example.threadsapp.viewModel.homeViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.PostModel.Quote
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuoteViewModel : ViewModel(){
    fun quoteThread(
        id: Int,
        text: String,
        onSuccess: () -> Unit
    ) {
        val apiInterface = RetrofitInstance.postApi
        val request = Quote(text, id)

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.createQuote(authHeader, request, id.toString())
        call.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    println("there was an error to quote a thread")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
            }
        })
    }
}