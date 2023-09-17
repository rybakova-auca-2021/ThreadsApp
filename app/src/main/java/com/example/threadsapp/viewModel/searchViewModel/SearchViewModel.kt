package com.example.threadsapp.viewModel.searchViewModel

import androidx.lifecycle.ViewModel
import com.example.threadsapp.api.RetrofitInstance
import com.example.threadsapp.model.ProfileModel.SearchHashtagResult
import com.example.threadsapp.model.SearchUserResult
import com.example.threadsapp.model.UserResult
import com.example.threadsapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    fun searchForUser(
        searchObj: String,
        onSuccess: (List<UserResult>) -> Unit,
        onError: (String) -> Unit
    ) {
        val apiInterface = RetrofitInstance.searchApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.searchUsers(authHeader, searchObj)
        call.enqueue(object : Callback<SearchUserResult> {
            override fun onResponse(call: Call<SearchUserResult>, response: Response<SearchUserResult>) {
                if (response.isSuccessful) {
                    val searchResults = response.body()
                    if (searchResults != null) {
                        onSuccess.invoke(searchResults.results)
                    }
                } else {
                    onError.invoke("Error fetching search results")
                }
            }

            override fun onFailure(call: Call<SearchUserResult>, t: Throwable) {
                onError.invoke("Network error")
            }
        })
    }


    fun searchForHashtag(
        searchObj: String,
        onSuccess: (List<SearchHashtagResult>) -> Unit,
        onError: (String) -> Unit
    ) {
        val apiInterface = RetrofitInstance.searchApi

        val token = Utils.token
        val authHeader = "Bearer $token"

        val call = apiInterface.searchHashtag(authHeader, searchObj)
        call.enqueue(object : Callback<List<SearchHashtagResult>> {
            override fun onResponse(call: Call<List<SearchHashtagResult>>, response: Response<List<SearchHashtagResult>>) {
                if (response.isSuccessful) {
                    val searchResults = response.body() ?: emptyList()
                    onSuccess.invoke(searchResults)
                } else {
                    onError.invoke("Error fetching hashtag results")
                }
            }

            override fun onFailure(call: Call<List<SearchHashtagResult>>, t: Throwable) {
                onError.invoke("Network error")
            }
        })
    }
}