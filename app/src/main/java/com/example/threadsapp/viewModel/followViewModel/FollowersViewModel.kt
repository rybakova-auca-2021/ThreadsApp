package com.example.threadsapp.viewModel.followViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.FollowerResult
import com.example.threadsapp.model.ProfileModel.Followers
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    fun followersList(
        id: Int,
        onSuccess: (List<Followers>) -> Unit,
        onError: (String) -> Unit
    ) {
        val apiInterface = RetrofitInstance.followApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.readFollowers(authHeader, id)
        call.enqueue(object : Callback<FollowerResult> {
            override fun onResponse(call: Call<FollowerResult>, response: Response<FollowerResult>) {
                if (response.isSuccessful) {
                    val results = response.body()
                    if (results != null) {
                        onSuccess.invoke(results.results)
                    }
                } else {
                    onError.invoke("Error fetching list of followers")
                }
            }

            override fun onFailure(call: Call<FollowerResult>, t: Throwable) {
                onError.invoke("Network error")
            }
        })
    }
}