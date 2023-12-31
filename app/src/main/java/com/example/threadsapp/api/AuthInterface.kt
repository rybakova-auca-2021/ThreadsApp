package com.example.threadsapp.api

import com.example.threadsapp.model.AuthModel.ConfirmEmail
import com.example.threadsapp.model.AuthModel.ConfirmEmailUpdate
import com.example.threadsapp.model.AuthModel.DetailResponse
import com.example.threadsapp.model.AuthModel.ForgotPassword
import com.example.threadsapp.model.AuthModel.ForgotPasswordUpdate
import com.example.threadsapp.model.AuthModel.ForgotPasswordVerify
import com.example.threadsapp.model.AuthModel.GoogleLogin
import com.example.threadsapp.model.AuthModel.Login
import com.example.threadsapp.model.AuthModel.LoginResponse
import com.example.threadsapp.model.AuthModel.LogoutRequest
import com.example.threadsapp.model.AuthModel.SignUp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthInterface {
    @POST("sign_in/")
    fun login(@Body request: Login): Call<LoginResponse>

    @POST("sign_in/google/")
    fun loginViaGoogle(@Body request: GoogleLogin): Call<Unit>

    @POST("sign_up/")
    fun signUp(@Body request: SignUp): Call<LoginResponse>

    @POST("forgot_password/")
    fun forgotPassword(@Body request: ForgotPassword): Call<DetailResponse>

    @PUT("forgot_password/update/")
    fun forgotPasswordUpdate(@Body request: ForgotPasswordUpdate): Call<DetailResponse>

    @POST("forgot_password/verify/")
    fun forgotPasswordVerify(@Body request: ForgotPasswordVerify): Call<DetailResponse>

    @POST("confirm_email/")
    fun confirmEmail(@Body request: ConfirmEmail): Call<DetailResponse>

    @PUT("confirm_email/update/")
    fun confirmEmailUpdate(@Body request: ConfirmEmailUpdate): Call<DetailResponse>

    @POST("logout/")
    fun logout(
        @Header("Authorization") token: String,
        @Body request: LogoutRequest
    ): Call<DetailResponse>
}