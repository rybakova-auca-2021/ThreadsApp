package com.example.threadsapp.viewModel.profileViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.HomeModel.PostModel
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPostsViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

    fun myPosts(
        onSuccess: (List<PostView>) -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading.value = true
        val apiInterface = RetrofitInstance.postApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.getPosts(authHeader)
        call.enqueue(object : Callback<PostModel> {
            override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    val postResults = response.body()
                    if (postResults != null) {
                        onSuccess.invoke(postResults.results)
                    }
                } else {
                    onError.invoke("Error fetching search results")
                }
            }

            override fun onFailure(call: Call<PostModel>, t: Throwable) {
                isLoading.value = false
                onError.invoke("Network error")
            }
        })
    }
}