package com.example.retrofit.network.model.chapterlist

data class ChapterListResponse(
    val result: String,
    val volumes: Map<String, Volumes>
)