package com.example.mangareaderui.domain.model

data class ChapterModel(
    val id: String?,
    val title: String?,
    val chapterPagesImageUrls: List<String>?,
    val chapter: String?,
    val pages: Int?,
    val date: String?
)
