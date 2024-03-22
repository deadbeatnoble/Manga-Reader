package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi

class FetchSearchedManga {
    suspend fun getSearchedManga(
        title: String
    ): MutableList<String> {
        val ids = mutableListOf<String>()
        val response = RetrofitApi(apiBaseUrl).service.getSearchedManga(title)

        for (data in response.data) {
            ids.add(data.id)
        }

        return ids
    }
}