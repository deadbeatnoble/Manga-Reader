package com.example.mangareaderui.domain.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDao {

    //to bookmark a manga
    @Insert
    suspend fun bookmarkManga(bookmarkEntity: BookmarkEntity)

    //to get all bookmarks
    @Query("SELECT * FROM bookmark")
    fun getAllBookmarks(): LiveData<List<BookmarkEntity>>

    //to check if a manga is bookmarked
    @Query("SELECT EXISTS(SELECT * FROM bookmark WHERE manga_id = :mangaId)")
    suspend fun isBookmarked(mangaId: String): Boolean

    //to remove bookmark
    @Query("DELETE FROM bookmark WHERE manga_id = :mangaId")
    suspend fun removeMangaBookmark(mangaId: String)
}