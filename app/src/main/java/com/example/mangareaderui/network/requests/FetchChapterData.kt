package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi

class FetchChapterData {
    suspend fun getChapterData(
        id: String
    ): MutableList<String> {
        val chapterPageUrlList= mutableListOf<String>()
        lateinit var baseUrl: String
        lateinit var hash: String

        val response = RetrofitApi(apiBaseUrl).service.getChapterData(id)

        baseUrl = response.baseUrl
        hash = response.chapter.hash

        for (data in response.chapter.data) {
            chapterPageUrlList.add("$baseUrl /data/ $hash / $data")
        }

        return chapterPageUrlList

    }
}