package com.example.mangareaderui.network.model.popular

data class PopularMangaResponse(
    val data: List<Data>,
    val limit: Int,
    val offset: Int,
    val response: String,
    val result: String,
    val total: Int
)