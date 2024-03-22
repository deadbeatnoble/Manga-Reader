package com.example.mangareaderui.network.model.chapterdata

data class ChapterDataResponse(
    val baseUrl: String,
    val chapter: Chapter,
    val result: String
)