package com.example.threadsapp.viewModel.postViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPostListViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    fun userPostList(
        userId: Int,
        onSuccess: (List<PostView>) -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading.value = true
        val apiInterface = RetrofitInstance.homeApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.getUserPosts(authHeader, userId)
        call.enqueue(object : Callback<List<PostView>> {
            override fun onResponse(call: Call<List<PostView>>, response: Response<List<PostView>>) {
                if (response.isSuccessful) {
                    val postResults = response.body()
                    if (postResults != null) {
                        onSuccess.invoke(postResults)
                    }
                    isLoading.value = false
                } else {
                    onError.invoke("Error fetching user posts")
                }
            }

            override fun onFailure(call: Call<List<PostView>>, t: Throwable) {
                isLoading.value = false
                onError.invoke("Network error")
            }
        })
    }
}