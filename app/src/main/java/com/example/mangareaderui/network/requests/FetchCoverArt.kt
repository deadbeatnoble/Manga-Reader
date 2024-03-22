package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi

class FetchCoverArt {
    suspend fun getCoverArt(
        mangaId: String,
        coverId: String
    ): String {
        lateinit var fileName: String

        val response = RetrofitApi(apiBaseUrl).service.getCoverArtId(coverId)
        fileName = response.data.attributes.fileName

        return "https://uploads.mangadex.org/covers/$mangaId/$fileName"
    }
}