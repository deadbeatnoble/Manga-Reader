package com.example.retrofit.network.model.chapterlist

data class Volumes(

    val volume: String,
    val count: Int,
    val chapters: Map<String, ChapterModel>
)