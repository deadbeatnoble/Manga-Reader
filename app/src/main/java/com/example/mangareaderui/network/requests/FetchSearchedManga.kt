package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi
import java.io.IOException
import java.net.SocketTimeoutException

class FetchSearchedManga {
    interface ResponseListener {
        fun onSuccess(datas: MutableList<String>)
        fun onError(message: String)
    }
    suspend fun getSearchedManga(
        title: String,
        responseListener: ResponseListener
    ) {
        try {
            val ids = mutableListOf<String>()
            val response = RetrofitApi(apiBaseUrl).service.getSearchedManga(title)

            for (data in response.data) {
                ids.add(data.id)
            }

            responseListener.onSuccess(datas = ids)
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