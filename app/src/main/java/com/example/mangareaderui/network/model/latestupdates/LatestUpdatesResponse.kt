package com.example.retrofit.network.model.latestupdates

data class LatestUpdatesResponse(
    val data: List<Data>,
    val limit: Int,
    val offset: Int,
    val response: String,
    val result: String,
    val total: Int
)