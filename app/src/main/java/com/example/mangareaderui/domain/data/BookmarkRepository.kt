package com.example.mangareaderui.domain.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {
    val getAllBookmarks: LiveData<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    suspend fun bookmarkManga(bookmarkEntity: BookmarkEntity) {
        bookmarkDao.bookmarkManga(bookmarkEntity)
    }

    suspend fun isBookmarked(mangaId: String): Boolean {
        return withContext(Dispatchers.IO) {
            bookmarkDao.isBookmarked(mangaId)
        }
    }

    suspend fun removeBookmark(mangaId: String) {
        bookmarkDao.removeMangaBookmark(mangaId = mangaId)
    }
}