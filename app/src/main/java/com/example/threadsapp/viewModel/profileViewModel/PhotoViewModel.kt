package com.example.threadsapp.viewModel.profileViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.util.LocalStorageProvider
import com.example.threadsapp.util.Utils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PhotoViewModel : ViewModel() {

    fun editPhoto(
        context: Context,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val photo = Utils.selectedImageUri
        val apiInterface = RetrofitInstance.profileApi

        val file: File? = photo?.let { LocalStorageProvider.getFile(context, it) }
        val requestBody = file?.asRequestBody("image/png".toMediaTypeOrNull())
        val imagePart = requestBody?.let {
            MultipartBody.Part.createFormData("photo", file.name, it)
        }

        imagePart?.let {
            apiInterface.editPhoto(
                it
            )
        }?.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onError.invoke()
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }
}