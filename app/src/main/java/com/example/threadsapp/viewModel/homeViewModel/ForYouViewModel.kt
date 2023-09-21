package com.example.threadsapp.viewModel.homeViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.HomeModel.PostModel
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForYouViewModel : ViewModel() {
    fun forYouPosts(
        onSuccess: (List<PostView>) -> Unit,
        onError: (String) -> Unit
    ) {
        val apiInterface = RetrofitInstance.homeApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.getForYouPosts(authHeader)
        call.enqueue(object : Callback<PostModel> {
            override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
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
                onError.invoke("Network error")
            }
        })
    }
}