package com.example.mangareaderui.network.requests

import android.util.Log
import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi

class FetchFinishedManga {
    suspend fun getFinishedManga(): MutableList<String> {

        val ids = mutableListOf<String>()
        val response = RetrofitApi(apiBaseUrl).service.getFinishedMangas()

        for (data in response.data) {
            Log.e("FINISHED_MANGA_IDS", data.id)
            ids.add(data.id)
        }

        return ids
    }
}