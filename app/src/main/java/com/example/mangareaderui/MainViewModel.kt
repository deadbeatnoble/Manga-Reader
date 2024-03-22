package com.example.mangareaderui

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mangareaderui.domain.model.ChapterModel
import com.example.mangareaderui.domain.model.MangaModel
import com.example.mangareaderui.screens.explorescreen.SearchWidgetState

class MainViewModel: ViewModel() {
    private val _searchWidgetUiState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetUiState: State<SearchWidgetState> = _searchWidgetUiState

    private val _searchTextUiState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextUiState: State<String> = _searchTextUiState

    private val _isSearchingState: MutableState<Boolean> =
        mutableStateOf(value = false)
    val isSearchingState: State<Boolean> = _isSearchingState

    @SuppressLint("MutableCollectionMutableState")
    private val _latestUpdatedManga: MutableState<MutableList<MangaModel>> =
        mutableStateOf(value = mutableStateListOf<MangaModel>().apply {
            repeat(10) {
                add(
                    MangaModel(
                        id = null,
                        name = null,
                        coverArtUrl = null,
                        genre = null,
                        description = null,
                        author = null,
                        status = null,
                        chapters = null
                    )
                )
            }
        })
    val latestUpdatedManga: State<MutableList<MangaModel>> = _latestUpdatedManga

    @SuppressLint("MutableCollectionMutableState")
    private val _finishedManga: MutableState<MutableList<MangaModel>> =
        mutableStateOf(value = mutableStateListOf<MangaModel>().apply {
            repeat(10) {
                add(
                    MangaModel(
                        id = null,
                        name = null,
                        coverArtUrl = null,
                        genre = null,
                        description = null,
                        author = null,
                        status = null,
                        chapters = null
                    )
                )
            }
        })
    val finishedManga: State<MutableList<MangaModel>> = _finishedManga

    @SuppressLint("MutableCollectionMutableState")
    private val _recentlyAddedManga: MutableState<MutableList<MangaModel>> =
        mutableStateOf(value = mutableStateListOf<MangaModel>().apply {
            repeat(10) {
                add(
                    MangaModel(
                        id = null,
                        name = null,
                        coverArtUrl = null,
                        genre = null,
                        description = null,
                        author = null,
                        status = null,
                        chapters = null
                    )
                )
            }
        })
    val recentlyAddedManga: State<MutableList<MangaModel>> = _recentlyAddedManga

    @SuppressLint("MutableCollectionMutableState")
    private val _trendyManga: MutableState<MutableList<MangaModel>> =
        mutableStateOf(value = mutableStateListOf<MangaModel>().apply {
            repeat(10) {
                add(
                    MangaModel(
                        id = null,
                        name = null,
                        coverArtUrl = null,
                        genre = null,
                        description = null,
                        author = null,
                        status = null,
                        chapters = null
                    )
                )
            }
        })
    val trendyManga: State<MutableList<MangaModel>> = _trendyManga

    @SuppressLint("MutableCollectionMutableState")
    private val _searchedManga: MutableState<MutableList<MangaModel>> =
        mutableStateOf(value = mutableStateListOf<MangaModel>().apply {
            repeat(10) {
                add(
                    MangaModel(
                        id = null,
                        name = null,
                        coverArtUrl = null,
                        genre = null,
                        description = null,
                        author = null,
                        status = null,
                        chapters = null
                    )
                )
            }
        })
    val searchedManga: State<MutableList<MangaModel>> = _searchedManga

    private val _mangaDetail: MutableState<MangaModel> =
        mutableStateOf(value =
            MangaModel(
                id = null,
                name = null,
                coverArtUrl = null,
                genre = null,
                description = null,
                author = null,
                status = null,
                chapters = null
            )
        )
    val mangaDetail: State<MangaModel> = _mangaDetail

    @SuppressLint("MutableCollectionMutableState")
    private val _chapterList: State<MutableList<ChapterModel>> =
        mutableStateOf(value = mutableStateListOf())
    val chapterList: State<List<ChapterModel>> = _chapterList

    @SuppressLint("MutableCollectionMutableState")
    private val _chapterLinks: State<MutableList<String>> =
        mutableStateOf(value = mutableStateListOf())
    val chapterLinks: State<List<String>> = _chapterLinks

    private val _latestUpdatedMangaIndex: MutableState<Int> =
        mutableStateOf(value = 0)

    private val _finishedMangaIndex: MutableState<Int> =
        mutableStateOf(value = 0)

    private val _recentlyAddedMangaIndex: MutableState<Int> =
        mutableStateOf(value = 0)

    private val _trendyMangaIndex: MutableState<Int> =
        mutableStateOf(value = 0)

    private val _searchedMangaIndex: MutableState<Int> =
        mutableStateOf(value = 0)


    //variable declarations are above and function declarations are below


    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetUiState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextUiState.value = newValue
    }

    fun updateIsSearchingState(newValue: Boolean) {
        _isSearchingState.value = newValue
    }

    fun loadLatestUpdatedManga(newValue: MangaModel) {
        if (_latestUpdatedMangaIndex.value < 10) {
            _latestUpdatedManga.value[_latestUpdatedMangaIndex.value] = newValue
        }
    }

    fun loadFinishedManga(newValue: MangaModel) {
        if (_finishedMangaIndex.value < 10) {
            _finishedManga.value[_finishedMangaIndex.value] = newValue
        }
    }

    fun loadRecentlyAddedManga(newValue: MangaModel) {
        if (_recentlyAddedMangaIndex.value < 10) {
            _recentlyAddedManga.value[_recentlyAddedMangaIndex.value] = newValue
        }
    }

    fun loadTrendyManga(newValue: MangaModel) {
        if (_trendyMangaIndex.value < 10) {
            _trendyManga.value[_trendyMangaIndex.value] = newValue
        }
    }

    fun loadSearchedManga(newValue: MangaModel) {
        if (_searchedMangaIndex.value < 10) {
            _searchedManga.value[_searchedMangaIndex.value] = newValue
        }
    }

    fun clearSearchedManga() {
        _searchedManga.value.clear()
        repeat(10) {
            _searchedManga.value.add(
                MangaModel(
                    id = null,
                    name = null,
                    coverArtUrl = null,
                    genre = null,
                    description = null,
                    author = null,
                    status = null,
                    chapters = null
                )
            )
        }
        _searchedMangaIndex.value = 0
    }

    fun loadMangaDetail(newValue: MangaModel) {
        _mangaDetail.value = newValue
    }

    fun loadChapterList(newValue: List<ChapterModel>) {
        _chapterList.value.clear()
        _chapterList.value.addAll(newValue)
    }

    fun clearChapterList() {
        _chapterList.value.clear()
    }

    fun loadChapterLinks(newValue: List<String>) {
        _chapterLinks.value.addAll(newValue)
    }

    fun clearChapterLinks() {
        _chapterLinks.value.clear()
    }

    fun updateLatestUpdatedMangaIndex() {
        _latestUpdatedMangaIndex.value++
    }

    fun updateFinishedMangaIndex() {
        _finishedMangaIndex.value++
    }

    fun updateRecentlyAddedMangaIndex() {
        _recentlyAddedMangaIndex.value++
    }

    fun updateTrendyMangaIndex() {
        _trendyMangaIndex.value++
    }

    fun updateSearchedMangaIndex() {
        _searchedMangaIndex.value++
    }

}