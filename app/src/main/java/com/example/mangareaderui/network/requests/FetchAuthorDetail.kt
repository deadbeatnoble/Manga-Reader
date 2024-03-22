package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi

class FetchAuthorDetail {
    suspend fun getAuthorDetail(
        id: String
    ): String {
        val response = RetrofitApi(apiBaseUrl).service.getAuthorDetail(id)

        return response.data.attributes.name
    }
}