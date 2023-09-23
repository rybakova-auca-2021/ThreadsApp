package com.example.neobis_android_chapter8.viewModels.AuthViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoViewModel : ViewModel() {
    val profileData: MutableLiveData<Profile?> = MutableLiveData()

    fun getInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiInterface = RetrofitInstance.profileApi
                val call = apiInterface.getUserInfo()

                call.enqueue(object : Callback<Profile> {
                    override fun onResponse(
                        call: Call<Profile>,
                        response: Response<Profile>
                    ) {
                        if (response.isSuccessful) {
                            val profile = response.body()
                            profileData.postValue(profile)
                            if (profile != null) {
                                fetchUserPhoto(profile.photo)
                            }
                        } else {
                        }
                    }
                    override fun onFailure(call: Call<Profile>, t: Throwable) {
                    }
                })
            } catch (e: Exception) {}
        }
    }

    private fun fetchUserPhoto(photoUrl: String?) {
        if (photoUrl != null) {
            profileData.value?.photo = photoUrl
        }
    }
}