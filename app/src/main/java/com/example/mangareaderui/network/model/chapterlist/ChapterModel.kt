package com.example.retrofit.network.model.chapterlist

data class ChapterModel(
    val chapter: String,
    val id: String,
    val others: List<String>,
    val count: Int
)