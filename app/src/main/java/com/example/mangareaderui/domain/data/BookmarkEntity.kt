package com.example.mangareaderui.domain.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val manga_id: String,
    val manga_name: String,
    val manga_coverArtUrl: String,
    val manga_genre: String,
    val manga_description: String,
    val manga_author: String,
    val manga_status: String,
    val manga_chapters: String
)