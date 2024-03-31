package com.example.mangareaderui.network.requests


import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi
import com.example.retrofit.network.model.mangadetail.MangaDetailResponse
import java.io.IOException
import java.net.SocketTimeoutException

class FetchMangaDetails {

    interface ResponseListener {
        fun onSuccess(mangaResponse: MangaDetailResponse)
        fun onError(message: String)
    }
    suspend fun getMangaDetail(
        id: String,
        responseListener: ResponseListener
    ) {

        try {
            val response = RetrofitApi(apiBaseUrl).service.getMangaDetail(id)

            responseListener.onSuccess(mangaResponse = response)
        } catch (e: SocketTimeoutException) {
            // Handle connection timeout exception
            // Display an error message or perform necessary actions
            responseListener.onError(message = "Connection timeout")
        } catch (e: IOException) {
            // Handle IO exception (no connection)
            // Display an error message or perform necessary actions
            responseListener.onError(message = "No network connection")
        } catch (e: Exception) {
            // Handle other general exceptions (wrong response, parsing errors, etc.)
            // Display an error message or perform necessary actions
            responseListener.onError(message = "Something went wrong")
        }
    }
}