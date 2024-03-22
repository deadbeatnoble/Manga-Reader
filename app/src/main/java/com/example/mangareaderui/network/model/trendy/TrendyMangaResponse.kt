package com.example.retrofit.network.model.trendy

data class TrendyMangaResponse(
    val data: List<Data>,
    val limit: Int,
    val offset: Int,
    val response: String,
    val result: String,
    val total: Int
)