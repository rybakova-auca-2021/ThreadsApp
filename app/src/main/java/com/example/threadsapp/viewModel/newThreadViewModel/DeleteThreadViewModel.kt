package com.example.threadsapp.viewModel.newThreadViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteThreadViewModel : ViewModel() {

    fun deletePost(
        id: Int,
        onSuccess: () -> Unit
    ) {
        val apiInterface = RetrofitInstance.postApi
        val token = Utils.token
        val authHeader = "Bearer $token"

        if (token != null) {
            apiInterface.postDelete(authHeader, id.toString()).enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        println("the was an error while deleting a post")
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    println("network error")
                }
            })
        }
    }
}
