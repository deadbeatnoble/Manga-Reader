package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi
import com.example.mangareaderui.network.model.chapterdetail.ChapterDetailResponse

class FetchChapterDetail {
    suspend fun getChapterDetail(
        id: String
    ): ChapterDetailResponse {

        return RetrofitApi(apiBaseUrl).service.getChapterDetail(id)
    }
}