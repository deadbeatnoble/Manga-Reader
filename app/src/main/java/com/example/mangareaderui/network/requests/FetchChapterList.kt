package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi
import java.io.IOException
import java.net.SocketTimeoutException

class FetchChapterList {
    interface ResponseListener {
        fun onSuccess(datas: MutableList<String>)
        fun onError(message: String)
    }
    suspend fun getChapterList(
        id: String,
        responseListener: ResponseListener
    ) {
        try {
            val chapterIdList = mutableListOf<String>()
            val response = RetrofitApi(apiBaseUrl).service.getChapterList(id)

            for (volume in response.volumes.values) {
                for (chapter in volume.chapters.values) {
                    chapterIdList.add(chapter.id)
                }
            }

            responseListener.onSuccess(datas = chapterIdList)
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