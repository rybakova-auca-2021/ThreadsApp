package com.example.threadsapp.api

import com.example.threadsapp.util.Constants.Companion.BASE_URL
import com.example.threadsapp.util.Utils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(AuthorizationInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val authApi by lazy {
            retrofit.create(AuthInterface::class.java)
        }

        val profileApi by lazy {
            retrofit.create(ProfileInterface::class.java)
        }

        val searchApi by lazy {
            retrofit.create(SearchInterface::class.java)
        }

        val homeApi by lazy {
            retrofit.create(MainInterface::class.java)
        }

        val postApi by lazy {
            retrofit.create(PostInterface::class.java)
        }

        private class AuthorizationInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request = chain.request()
                val newRequest = if (requiresAuthorization(request)) {
                    val token = Utils.token
                    val authHeader = "Bearer $token"
                    request.newBuilder()
                        .header("Authorization", authHeader)
                        .build()
                } else {
                    request
                }
                return chain.proceed(newRequest)
            }

            private fun requiresAuthorization(request: okhttp3.Request): Boolean {
                val path = request.url.encodedPath
                return  path.endsWith("forgot_password/verify/") ||
                        path.endsWith("confirm_email/") ||
                        path.endsWith("me/") ||
                        path.endsWith("update/") ||
                        path.endsWith("edit_photo/") ||
                        path.endsWith("post/")
            }
        }
    }
}

