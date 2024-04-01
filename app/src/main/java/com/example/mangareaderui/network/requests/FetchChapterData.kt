package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi
import java.io.IOException
import java.net.SocketTimeoutException

class FetchChapterData {
    interface ResponseListener {
        fun onSuccess(datas: MutableList<String>)
        fun onError(message: String)
    }
    suspend fun getChapterData(
        id: String,
        responseListener: ResponseListener
    ) {
        try {
            val chapterPageUrlList = mutableListOf<String>()
            lateinit var baseUrl: String
            lateinit var hash: String

            val response = RetrofitApi(apiBaseUrl).service.getChapterData(id)

            baseUrl = response.baseUrl
            hash = response.chapter.hash

            for (data in response.chapter.data) {
                chapterPageUrlList.add("$baseUrl/data/$hash/$data")
            }

            responseListener.onSuccess(datas = chapterPageUrlList)
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
            responseListener.onError(message = "Parsing error")
        }

    }
}