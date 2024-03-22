package com.example.mangareaderui.network.requests

import android.util.Log
import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi

class FetchRecentlyAddedManga {
    suspend fun getRecentlyAddedManga(): MutableList<String> {
        val ids = mutableListOf<String>()
        val response = RetrofitApi(apiBaseUrl).service.getRecentlyAddedMangas()

        for (data in response.data) {
            if (!ids.contains(data.id)) {
                Log.e("RECENTLY_ADDED_MANGA_IDS", data.id)
                ids.add(data.id)
            }
        }


        return ids
    }
}