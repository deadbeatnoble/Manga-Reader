package com.example.mangareaderui.network.requests

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi
import java.io.IOException
import java.net.SocketTimeoutException

class FetchLatestUpdates {
    interface ResponseListener {
        fun onSuccess(datas: MutableList<String>)
        fun onError(message: String)
    }
    suspend fun getLatestUpdatesIds(
        responseListener: ResponseListener
    ) {

        try {
            val ids = mutableListOf<String>()
            val offset =
                mutableStateOf(0)


            ids.clear()
            while (ids.size < 10){
                val response = RetrofitApi(apiBaseUrl).service.getLatestUpdates(offset = offset.value)
                for (data in response.data) {
                    for (relationship in data.relationships){
                        when(relationship.type) {
                            "manga" -> {
                                if (!ids.contains(relationship.id)) {
                                    if (ids.size == 10) {
                                        break
                                    }
                                    ids.add(relationship.id)
                                } else {
                                    continue
                                }
                            }
                            //Log.e("LATEST UPDATES RESPONSE", relationship.id)
                            else -> continue
                        }
                    }
                }

                if (ids.size < 10) {
                    offset.value += 10
                } else {
                    offset.value = 0
                    break
                }
            }

            for (id in ids) {
                Log.e("LATEST_MANGA_IDS", id)
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