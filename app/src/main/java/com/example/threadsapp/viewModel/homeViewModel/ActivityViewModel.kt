package com.example.threadsapp.viewModel.homeViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.HomeModel.Notification
import com.example.threadsapp.model.HomeModel.NotificationResponse
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

    fun getNotifications(
        onSuccess: (List<Notification>) -> Unit
    ) {
        isLoading.value = true
        val apiInterface = RetrofitInstance.homeApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.getNotifications(authHeader)
        call.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                isLoading.value = !response.isSuccessful
                if (response.isSuccessful) {
                    val results = response.body()
                    if (results != null) {
                        onSuccess.invoke(results.results)
                    }
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                isLoading.value = false
            }
        })
    }

    fun getNotificationsByType(
        type: String,
        onSuccess: (List<Notification>) -> Unit
    ) {
        isLoading.value = true
        val apiInterface = RetrofitInstance.homeApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.getNotificationsByType(authHeader, type)
        call.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                isLoading.value = !response.isSuccessful
                if (response.isSuccessful) {
                    val results = response.body()
                    if (results != null) {
                        onSuccess.invoke(results.results)
                    }
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                isLoading.value = false
            }
        })
    }
}
