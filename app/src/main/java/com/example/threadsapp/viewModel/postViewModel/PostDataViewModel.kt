package com.example.threadsapp.viewModel.postViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDataViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

    fun readData(
        id: Int,
        onSuccess: (PostView) -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading.value = true
        val apiInterface = RetrofitInstance.postApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.postRead(authHeader, id.toString())
        call.enqueue(object : Callback<PostView> {
            override fun onResponse(call: Call<PostView>, response: Response<PostView>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    val postResults = response.body()
                    if (postResults != null) {
                        onSuccess.invoke(postResults)
                    }
                } else {
                    onError.invoke("Error fetching search results")
                }
            }

            override fun onFailure(call: Call<PostView>, t: Throwable) {
                isLoading.value = false
                onError.invoke("Network error")
            }
        })
    }
}