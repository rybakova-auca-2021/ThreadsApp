package com.example.neobis_android_chapter8.viewModels.AuthViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoViewModel : ViewModel() {
    val profileData: MutableLiveData<Profile> = MutableLiveData()

    fun getInfo() {
        val apiInterface = RetrofitInstance.profileApi
        val call = apiInterface.getUserInfo()

        call.enqueue(object : Callback<Profile> {
            override fun onResponse(
                call: Call<Profile>,
                response: Response<Profile>
            ) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    profileData.value = profile!!
                    fetchUserPhoto(profile.photo)
                } else {
                }
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
            }
        })
    }

    private fun fetchUserPhoto(photoUrl: String?) {
        if (photoUrl != null) {
            profileData.value?.photo = photoUrl
        }
    }
}