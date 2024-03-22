package com.example.mangareaderui.domain.model

data class MangaModel(
    val id: String?,
    val name: String?,
    val coverArtUrl: String?,
    val genre: List<String>?,
    val description: String?,
    val author: List<String>?,
    val status: String?,
    val chapters: List<ChapterModel>?
)
