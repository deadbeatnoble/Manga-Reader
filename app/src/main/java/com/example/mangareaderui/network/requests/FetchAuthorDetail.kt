package com.example.mangareaderui.network.requests

import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi
import java.io.IOException
import java.net.SocketTimeoutException

class FetchAuthorDetail {
    interface ResponseListener {
        fun onSuccess(data: String)
        fun onError(message: String)
    }
    suspend fun getAuthorDetail(
        id: String,
        responseListener: ResponseListener
    ) {
        try {
            val response = RetrofitApi(apiBaseUrl).service.getAuthorDetail(id)

            responseListener.onSuccess(data = response.data.attributes.name)
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