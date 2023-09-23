package com.example.threadsapp.viewModel.profileViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.Profile
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SomeoneProfileViewModel : ViewModel() {
    fun getUserProfile(username: String, onSuccess: (Profile) -> Unit, onError: (String) -> Unit) {
        val apiInterface = RetrofitInstance.profileApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.getUserProfile(authHeader, username)
        call.enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    if (userProfile != null) {
                        onSuccess.invoke(userProfile)
                    }
                } else {
                    onError.invoke("Error fetching user profile")
                }
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
                onError.invoke("Network error")
            }
        })
    }

    fun getUserProfileById(id: Int, onSuccess: (Profile) -> Unit, onError: (String) -> Unit) {
        val apiInterface = RetrofitInstance.profileApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.getUserProfileById(authHeader, id)
        call.enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    if (userProfile != null) {
                        onSuccess.invoke(userProfile)
                    }
                } else {
                    onError.invoke("Error fetching user profile")
                }
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
                onError.invoke("Network error")
            }
        })
    }

}
