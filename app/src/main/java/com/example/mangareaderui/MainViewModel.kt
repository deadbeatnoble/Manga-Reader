package com.example.mangareaderui

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mangareaderui.domain.model.ChapterModel
import com.example.mangareaderui.domain.model.MangaModel
import com.example.mangareaderui.network.requests.FetchAuthorDetail
import com.example.mangareaderui.network.requests.FetchCoverArt
import com.example.mangareaderui.network.requests.FetchFinishedManga
import com.example.mangareaderui.network.requests.FetchLatestUpdates
import com.example.mangareaderui.network.requests.FetchMangaDetails
import com.example.mangareaderui.network.requests.FetchPopularManga
import com.example.mangareaderui.network.requests.FetchRecentlyAddedManga
import com.example.mangareaderui.network.requests.FetchTrendyManga
import com.example.mangareaderui.screens.explorescreen.SearchWidgetState
import com.example.mangareaderui.screens.mainscreen.getMangaAuthor
import com.example.mangareaderui.screens.mainscreen.getMangaCoverArtId
import com.example.mangareaderui.screens.mainscreen.getMangaDescription
import com.example.mangareaderui.screens.mainscreen.getMangaName
import com.example.mangareaderui.screens.mainscreen.getMangaTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _screenWidth: MutableState<Int> =
        mutableStateOf(value = 0)
    val screenWidth: State<Int> = _screenWidth

    private val _screenHeight: MutableState<Int> =
        mutableStateOf(value = 0)
    val screenHeight: State<Int> = _screenHeight

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
    private val _latestUpdatedManga = MutableLiveData<MutableList<MangaModel>>().apply {
        value = MutableList(10) {
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
        }
    }
    val latestUpdatedManga: LiveData<MutableList<MangaModel>> = _latestUpdatedManga

    @SuppressLint("MutableCollectionMutableState")
    private val _finishedManga = MutableLiveData<MutableList<MangaModel>>().apply {
        value = MutableList(10) {
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
        }
    }
    val finishedManga: LiveData<MutableList<MangaModel>> = _finishedManga

    @SuppressLint("MutableCollectionMutableState")
    private val _recentlyAddedManga = MutableLiveData<MutableList<MangaModel>>().apply {
        value = MutableList(10) {
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
        }
    }
    val recentlyAddedManga: LiveData<MutableList<MangaModel>> = _recentlyAddedManga

    @SuppressLint("MutableCollectionMutableState")
    private val _trendyManga = MutableLiveData<MutableList<MangaModel>>().apply {
        value = MutableList(10) {
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
        }
    }
    val trendyManga: LiveData<MutableList<MangaModel>> = _trendyManga

    @SuppressLint("MutableCollectionMutableState")
    private val _popularManga = MutableLiveData<MutableList<MangaModel>>().apply {
        value = MutableList(10) {
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
        }
    }
    val popularManga: LiveData<MutableList<MangaModel>> = _popularManga

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

    private val _popularMangaIndex: MutableState<Int> =
        mutableStateOf(value = 0)

    private val _searchedMangaIndex: MutableState<Int> =
        mutableStateOf(value = 0)


    //variable declarations are above and function declarations are below

    fun setScreenSize(width: Int, height: Int) {
        _screenWidth.value = width
        _screenHeight.value = height
    }

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
            _latestUpdatedManga.value?.set(_latestUpdatedMangaIndex.value, newValue)
        }
    }

    fun loadFinishedManga(newValue: MangaModel) {
        if (_finishedMangaIndex.value < 10) {
            _finishedManga.value?.set(_finishedMangaIndex.value, newValue)
        }
    }

    fun loadRecentlyAddedManga(newValue: MangaModel) {
        if (_recentlyAddedMangaIndex.value < 10) {
            _recentlyAddedManga.value?.set(_recentlyAddedMangaIndex.value, newValue)
        }
    }

    fun loadTrendyManga(newValue: MangaModel) {
        if (_trendyMangaIndex.value < 10) {
            _trendyManga.value?.set(_trendyMangaIndex.value, newValue)
        }
    }

    fun loadPopularManga(newValue: MangaModel) {
        if (_popularMangaIndex.value < 10) {
            _popularManga.value?.set(_popularMangaIndex.value, newValue)
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

    fun updatePopularMangaIndex() {
        _popularMangaIndex.value++
    }

    fun updateSearchedMangaIndex() {
        _searchedMangaIndex.value++
    }

    suspend fun fetchData() {
        val latestUpdatedIds = FetchLatestUpdates().getLatestUpdatesIds()
        val finishedIds = FetchFinishedManga().getFinishedManga()
        val recentlyAddedIds = FetchRecentlyAddedManga().getRecentlyAddedManga()
        val trendyIds = FetchTrendyManga().getTrendyManga()
        val popularIds = FetchPopularManga().getPopularManga()

        CoroutineScope(IO).launch {
            for (id in latestUpdatedIds) {
                val mangaResponse = FetchMangaDetails().getMangaDetail(id = id)

                val coverArtUrl = FetchCoverArt().getCoverArt(
                    mangaId = mangaResponse.data.id,
                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                )

                val author = FetchAuthorDetail().getAuthorDetail(
                    id = getMangaAuthor(mangaResponse)
                )

                loadLatestUpdatedManga(newValue = MangaModel(
                    id = id,
                    name = getMangaName(mangaResponse = mangaResponse),
                    coverArtUrl = coverArtUrl,
                    genre = getMangaTags(mangaResponse = mangaResponse),
                    description = getMangaDescription(mangaResponse = mangaResponse),
                    author = listOf(author),
                    status = mangaResponse.data.attributes.status,
                    chapters = emptyList()
                )
                )
                updateLatestUpdatedMangaIndex()
            }
        }
        CoroutineScope(IO).launch {
            for (id in finishedIds) {
                val mangaResponse = FetchMangaDetails().getMangaDetail(id = id)

                val coverArtUrl = FetchCoverArt().getCoverArt(
                    mangaId = mangaResponse.data.id,
                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                )

                val author = FetchAuthorDetail().getAuthorDetail(
                    id = getMangaAuthor(mangaResponse)
                )

                loadFinishedManga(newValue = MangaModel(
                    id = id,
                    name = getMangaName(mangaResponse = mangaResponse),
                    coverArtUrl = coverArtUrl,
                    genre = getMangaTags(mangaResponse = mangaResponse),
                    description = getMangaDescription(mangaResponse = mangaResponse),
                    author = listOf(author),
                    status = mangaResponse.data.attributes.status,
                    chapters = emptyList()
                ))
                updateFinishedMangaIndex()
            }
        }
        CoroutineScope(IO).launch {
            for (id in recentlyAddedIds) {
                val mangaResponse = FetchMangaDetails().getMangaDetail(id = id)

                val coverArtUrl = FetchCoverArt().getCoverArt(
                    mangaId = mangaResponse.data.id,
                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                )

                val author = FetchAuthorDetail().getAuthorDetail(
                    id = getMangaAuthor(mangaResponse)
                )


                loadRecentlyAddedManga(newValue = MangaModel(
                    id = id,
                    name = getMangaName(mangaResponse = mangaResponse),
                    coverArtUrl = coverArtUrl,
                    genre = getMangaTags(mangaResponse = mangaResponse),
                    description = getMangaDescription(mangaResponse = mangaResponse),
                    author = listOf(author),
                    status = mangaResponse.data.attributes.status,
                    chapters = emptyList()
                ))
                updateRecentlyAddedMangaIndex()
            }
        }
        CoroutineScope(IO).launch {
            for (id in trendyIds) {
                val mangaResponse = FetchMangaDetails().getMangaDetail(id = id)

                val coverArtUrl = FetchCoverArt().getCoverArt(
                    mangaId = mangaResponse.data.id,
                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                )

                val author = FetchAuthorDetail().getAuthorDetail(
                    id = getMangaAuthor(mangaResponse)
                )


                loadTrendyManga(newValue = MangaModel(
                    id = id,
                    name = getMangaName(mangaResponse = mangaResponse),
                    coverArtUrl = coverArtUrl,
                    genre = getMangaTags(mangaResponse = mangaResponse),
                    description = getMangaDescription(mangaResponse = mangaResponse),
                    author = listOf(author),
                    status = mangaResponse.data.attributes.status,
                    chapters = emptyList()
                ))
                updateTrendyMangaIndex()
            }
        }
        CoroutineScope(IO).launch {
            for (id in popularIds) {
                val mangaResponse = FetchMangaDetails().getMangaDetail(id = id)

                val coverArtUrl = FetchCoverArt().getCoverArt(
                    mangaId = mangaResponse.data.id,
                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                )

                val author = FetchAuthorDetail().getAuthorDetail(
                    id = getMangaAuthor(mangaResponse)
                )


                loadPopularManga(newValue = MangaModel(
                    id = id,
                    name = getMangaName(mangaResponse = mangaResponse),
                    coverArtUrl = coverArtUrl,
                    genre = getMangaTags(mangaResponse = mangaResponse),
                    description = getMangaDescription(mangaResponse = mangaResponse),
                    author = listOf(author),
                    status = mangaResponse.data.attributes.status,
                    chapters = emptyList()
                ))
                updatePopularMangaIndex()
            }
        }
    }
}