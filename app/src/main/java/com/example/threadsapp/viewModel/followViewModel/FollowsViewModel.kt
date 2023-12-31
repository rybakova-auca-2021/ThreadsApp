package com.example.threadsapp.viewModel.followViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.FollowerResult
import com.example.threadsapp.model.ProfileModel.Followers
import com.example.threadsapp.model.ProfileModel.Follows
import com.example.threadsapp.model.ProfileModel.FollowsResult
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowsViewModel : ViewModel() {
    fun followsList(
        id: Int,
        onSuccess: (List<Follows>) -> Unit,
        onError: (String) -> Unit
    ) {
        val apiInterface = RetrofitInstance.followApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.readFollows(authHeader, id)
        call.enqueue(object : Callback<FollowsResult> {
            override fun onResponse(call: Call<FollowsResult>, response: Response<FollowsResult>) {
                if (response.isSuccessful) {
                    val results = response.body()
                    if (results != null) {
                        onSuccess.invoke(results.results)
                    }
                } else {
                    onError.invoke("Error fetching list of followers")
                }
            }

            override fun onFailure(call: Call<FollowsResult>, t: Throwable) {
                onError.invoke("Network error")
            }
        })
    }
}