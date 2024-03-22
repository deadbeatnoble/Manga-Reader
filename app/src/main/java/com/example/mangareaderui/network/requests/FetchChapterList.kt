package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi

class FetchChapterList {
    suspend fun getChapterList(
        id: String
    ): MutableList<String> {

        val chapterIdList = mutableListOf<String>()

        val response = RetrofitApi(apiBaseUrl).service.getChapterList(id)

        for (volume in response.volumes.values) {
            for (chapter in volume.chapters.values) {
                chapterIdList.add(chapter.id)
            }
        }


        return chapterIdList

    }
}