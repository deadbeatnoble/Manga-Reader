package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi
import com.example.mangareaderui.network.model.chapterdetail.ChapterDetailResponse
import java.io.IOException
import java.net.SocketTimeoutException

class FetchChapterDetail {
    interface ResponseListener {
        fun onSuccess(data: ChapterDetailResponse)
        fun onError(message: String)
    }
    suspend fun getChapterDetail(
        id: String,
        responseListener: ResponseListener
    ) {

        try {
            val response = RetrofitApi(apiBaseUrl).service.getChapterDetail(id)

            responseListener.onSuccess(data = response)
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