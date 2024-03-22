package com.example.mangareaderui.network.requests

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.mangareaderui.network.BaseUrls.apiBaseUrl
import com.example.mangareaderui.network.RetrofitApi

class FetchLatestUpdates {
    suspend fun getLatestUpdatesIds(): MutableList<String>{

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

        return ids
    }
}