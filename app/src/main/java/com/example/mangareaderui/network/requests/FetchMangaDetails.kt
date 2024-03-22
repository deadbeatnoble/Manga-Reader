package com.example.mangareaderui.network.requests


import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi
import com.example.retrofit.network.model.mangadetail.MangaDetailResponse

class FetchMangaDetails {
    suspend fun getMangaDetail(
        id: String
    ): MangaDetailResponse {

        return RetrofitApi(apiBaseUrl).service.getMangaDetail(id)
    }
}