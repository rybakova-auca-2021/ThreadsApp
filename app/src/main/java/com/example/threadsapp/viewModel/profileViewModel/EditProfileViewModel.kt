package com.example.threadsapp.viewModel.profileViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.Profile
import com.example.threadsapp.model.ProfileModel.UpdateRequest
import com.example.threadsapp.util.Utils.Companion.token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel : ViewModel() {

    fun updateProfile(
        username: String,
        fullName: String,
        bio: String,
        website: String,
        isPrivate: Boolean
    ) {
        val apiInterface = RetrofitInstance.profileApi
        val request = UpdateRequest(username, fullName, bio, website, isPrivate)

        token?.let { apiInterface.update(request) }?.enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {}

            override fun onFailure(call: Call<Profile>, t: Throwable) {}
        })
    }
}