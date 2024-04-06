package com.example.mangareaderui.domain.data

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mangareaderui.domain.model.MangaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application): AndroidViewModel(application) {
    private val getAllBookmarks: LiveData<List<BookmarkEntity>>
    private val repository: BookmarkRepository

    init {
        val bookmarkDao = BookmarkDatabase.getDatabase(application).bookmarkDao()
        repository = BookmarkRepository(bookmarkDao)
        getAllBookmarks = repository.getAllBookmarks
    }

    private fun bookmarkManga(bookmarkEntity: BookmarkEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.bookmarkManga(bookmarkEntity = bookmarkEntity)
        }
    }

    private suspend fun removeBookmark(mangaId: String) {
        repository.removeBookmark(mangaId = mangaId)
    }

    fun bookmarkOnClick(bookmarkEntity: BookmarkEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.isBookmarked(mangaId = bookmarkEntity.manga_id)) {
                removeBookmark(mangaId = bookmarkEntity.manga_id)
            } else {
                bookmarkManga(bookmarkEntity = bookmarkEntity)
            }

            isBookmarked(mangaId = bookmarkEntity.manga_id)
        }
    }

    private var _isBookmarkedState = MutableStateFlow(value = false)
    val isBookmarkedState = _isBookmarkedState.asStateFlow()

    fun isBookmarked(mangaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isBookmarkedState.value = repository.isBookmarked(mangaId = mangaId)
        }
    }

    private var _bookmarkedMangas = MutableStateFlow(value = mutableListOf<MangaModel>())
    val bookmarkedMangas = _bookmarkedMangas.asStateFlow()
    private val _bookmarkedMangasIndex: MutableState<Int> =
        mutableStateOf(value = 0)

    fun loadBookmarks() {
        getAllBookmarks.observeForever { bookmarks ->
            bookmarks?.let {
                _bookmarkedMangas.value = it.map { bookmarkedManga ->
                    MangaModel(
                        id = bookmarkedManga.manga_id,
                        name = bookmarkedManga.manga_name,
                        coverArtUrl = bookmarkedManga.manga_coverArtUrl,
                        genre = stringToList(bookmarkedManga.manga_genre),
                        description = bookmarkedManga.manga_description,
                        author = listOf(stringToList(bookmarkedManga.manga_author).first()),
                        status = bookmarkedManga.manga_status,
                        chapters = emptyList()
                    )
                }.toMutableList()
            }
        }
    }



    private fun stringToList(string: String): List<String> {
        val trimmedString = string.trim('[', ']') // Remove leading and trailing square brackets
        return trimmedString.split(", ") // Split the trimmed string using the comma and space as delimiters
    }

}