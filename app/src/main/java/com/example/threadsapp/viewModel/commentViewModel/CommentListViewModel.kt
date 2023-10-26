package com.example.threadsapp.viewModel.commentViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.PostModel.CommentResponse
import com.example.threadsapp.model.PostModel.CommentView
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentListViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

    fun commentsList(
        id: String,
        onSuccess: (List<CommentView>, Int) -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading.value = true
        val apiInterface = RetrofitInstance.postApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.postCommentsList(authHeader, id)
        call.enqueue(object : Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    val results = response.body()
                    if (results != null) {
                        onSuccess.invoke(results.results, results.count)
                    }
                } else {
                    onError.invoke("Error fetching comment results")
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                isLoading.value = false
                onError.invoke("Network error")
            }
        })
    }
}