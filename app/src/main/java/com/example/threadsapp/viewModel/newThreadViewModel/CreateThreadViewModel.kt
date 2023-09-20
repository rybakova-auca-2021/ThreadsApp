package com.example.threadsapp.viewModel.newThreadViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.util.LocalStorageProvider
import com.example.threadsapp.util.Utils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class CreateThreadViewModel : ViewModel() {

    fun createPost(
        context: Context,
        text: String,
        comment: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val token = Utils.token
        val apiInterface = RetrofitInstance.postApi

        val imageUri = Utils.selectedImageUri
        val videoUri = Utils.selectedVideoUri

        val imageFile: File? = imageUri?.let { LocalStorageProvider.getFile(context, it) }
        val videoFile: File? = videoUri?.let { LocalStorageProvider.getFile(context, it) }

        val textPart = text.toRequestBody("text/plain".toMediaTypeOrNull())
        val commentPart = comment.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = imageFile?.asRequestBody("image/*".toMediaTypeOrNull())?.let {
            MultipartBody.Part.createFormData("photo", imageFile.name, it)
        }
        val videoPart = videoFile?.asRequestBody("video/*".toMediaTypeOrNull())?.let {
            MultipartBody.Part.createFormData("video", videoFile.name, it)
        }

        if (token != null) {
            apiInterface.createPost(
                token,
                textPart,
                imagePart,
                videoPart,
                commentPart
            ).enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        onError()
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    onError()
                }
            })
        }
    }
}
