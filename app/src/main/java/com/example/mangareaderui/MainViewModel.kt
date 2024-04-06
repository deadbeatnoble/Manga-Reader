package com.example.mangareaderui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangareaderui.domain.model.ChapterModel
import com.example.mangareaderui.domain.model.MangaModel
import com.example.mangareaderui.network.model.chapterdetail.ChapterDetailResponse
import com.example.mangareaderui.network.requests.FetchAuthorDetail
import com.example.mangareaderui.network.requests.FetchChapterData
import com.example.mangareaderui.network.requests.FetchChapterDetail
import com.example.mangareaderui.network.requests.FetchChapterList
import com.example.mangareaderui.network.requests.FetchCoverArt
import com.example.mangareaderui.network.requests.FetchFinishedManga
import com.example.mangareaderui.network.requests.FetchLatestUpdates
import com.example.mangareaderui.network.requests.FetchMangaDetails
import com.example.mangareaderui.network.requests.FetchPopularManga
import com.example.mangareaderui.network.requests.FetchRecentlyAddedManga
import com.example.mangareaderui.network.requests.FetchSearchedManga
import com.example.mangareaderui.network.requests.FetchTrendyManga
import com.example.mangareaderui.screens.explorescreen.SearchWidgetState
import com.example.mangareaderui.screens.mainscreen.getMangaAuthor
import com.example.mangareaderui.screens.mainscreen.getMangaCoverArtId
import com.example.mangareaderui.screens.mainscreen.getMangaDescription
import com.example.mangareaderui.screens.mainscreen.getMangaName
import com.example.mangareaderui.screens.mainscreen.getMangaTags
import com.example.retrofit.network.model.mangadetail.MangaDetailResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val _latestUpdatedManga = MutableStateFlow(
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
    )
    val latestUpdatedManga: StateFlow<MutableList<MangaModel>> = _latestUpdatedManga.asStateFlow()

    private var _isLatestUpdatedMangasLoading = MutableStateFlow(value = true)
    val isLatestUpdatedMangasLoading = _isLatestUpdatedMangasLoading.asStateFlow()

    private var _latestUpdatedMangasError = MutableStateFlow(value = "")
    val latestUpdatedMangasError = _latestUpdatedMangasError.asStateFlow()

    @SuppressLint("MutableCollectionMutableState")
    private val _finishedManga = MutableStateFlow(
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
    )
    val finishedManga: StateFlow<MutableList<MangaModel>> = _finishedManga.asStateFlow()

    private var _isFinishedMangasLoading = MutableStateFlow(value = true)
    val isFinishedMangasLoading = _isFinishedMangasLoading.asStateFlow()

    private var _finishedMangasError = MutableStateFlow(value = "")
    val finishedMangasError = _finishedMangasError.asStateFlow()

    @SuppressLint("MutableCollectionMutableState")
    private val _recentlyAddedManga = MutableStateFlow(
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
    )
    val recentlyAddedManga: StateFlow<MutableList<MangaModel>> = _recentlyAddedManga.asStateFlow()

    private var _isRecentlyAddedMangasLoading = MutableStateFlow(value = true)
    val isRecentlyAddedMangasLoading = _isLatestUpdatedMangasLoading.asStateFlow()

    private var _recentlyAddedMangasError = MutableStateFlow(value = "")
    val recentlyAddedMangasError = _recentlyAddedMangasError.asStateFlow()

    @SuppressLint("MutableCollectionMutableState")
    private val _trendyManga = MutableStateFlow(
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
    )
    val trendyManga: StateFlow<MutableList<MangaModel>> = _trendyManga.asStateFlow()

    private var _trendyMangasError = MutableStateFlow(value = "")
    val trendyMangasError = _trendyMangasError.asStateFlow()

    @SuppressLint("MutableCollectionMutableState")
    private val _popularManga = MutableStateFlow(
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
    )
    val popularManga: StateFlow<MutableList<MangaModel>> = _popularManga.asStateFlow()

    private var _isPopularMangasLoading = MutableStateFlow(value = true)
    val isPopularMangasLoading = _isPopularMangasLoading.asStateFlow()

    private var _popularMangasError = MutableStateFlow(value = "")
    val popularMangasError = _popularMangasError.asStateFlow()

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

    private var _searchedMangasError = MutableStateFlow(value = "")
    val searchedMangasError = _searchedMangasError.asStateFlow()

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
    private val _chapterList = MutableStateFlow<MutableList<ChapterModel>>(
        value = mutableStateListOf()
    )
    val chapterList: StateFlow<List<ChapterModel>> = _chapterList.asStateFlow()

    private val _chapterListError: MutableState<String> =
        mutableStateOf(value = "")
    val chapterListError: State<String> = _chapterListError

    private val _selectedChapterId = MutableStateFlow(value = "")
    val selectedChapterId: StateFlow<String> = _selectedChapterId

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
            _latestUpdatedManga.value[_latestUpdatedMangaIndex.value] = newValue
            _latestUpdatedMangaIndex.value++
        }
    }

    fun loadFinishedManga(newValue: MangaModel) {
        if (_finishedMangaIndex.value < 10) {
            _finishedManga.value[_finishedMangaIndex.value] = newValue
            _finishedMangaIndex.value++
        }
    }

    fun loadRecentlyAddedManga(newValue: MangaModel) {
        if (_recentlyAddedMangaIndex.value < 10) {
            _recentlyAddedManga.value[_recentlyAddedMangaIndex.value] = newValue
            _recentlyAddedMangaIndex.value++
        }
    }

    fun loadTrendyManga(newValue: MangaModel) {
        if (_trendyMangaIndex.value < 10) {
            _trendyManga.value?.set(_trendyMangaIndex.value, newValue)
        }
    }

    fun loadPopularManga(newValue: MangaModel) {
        if (_popularMangaIndex.value < 10) {
            _popularManga.value[_popularMangaIndex.value] = newValue
            _popularMangaIndex.value++
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

    fun loadChapterList(newValue: ChapterModel) {
        _chapterList.value.add(newValue)
    }

    fun loadChapterListError(newValue: String) {
        _chapterListError.value = newValue
    }

    fun clearChapterList() {
        _chapterList.value.clear()
    }

    fun loadSelectedChapterId(newValue: String) {
        _selectedChapterId.value = newValue
    }

    fun loadChapterLinks(newValue: List<String>) {
        _chapterLinks.value.addAll(newValue)
    }

    fun clearChapterLinks() {
        _chapterLinks.value.clear()
    }

    fun updateTrendyMangaIndex() {
        _trendyMangaIndex.value++
    }
    fun updateSearchedMangaIndex() {
        _searchedMangaIndex.value++
    }

    fun updateSearchedMangaError() {
        _searchedMangasError.value = ""
    }

    fun fetchData() {
        CoroutineScope(IO).launch{
            _isLatestUpdatedMangasLoading.value = true


            FetchLatestUpdates().getLatestUpdatesIds(
                responseListener = object : FetchLatestUpdates.ResponseListener {
                    override fun onSuccess(datas: MutableList<String>) {
                        CoroutineScope(IO).launch {
                            for (id in datas) {
                                FetchMangaDetails().getMangaDetail(
                                    id = id,
                                    responseListener = object : FetchMangaDetails.ResponseListener {
                                        override fun onSuccess(mangaResponse: MangaDetailResponse) {

                                            val coverArtUrl = async {
                                                lateinit var url: String

                                                FetchCoverArt().getCoverArt(
                                                    mangaId = mangaResponse.data.id,
                                                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse),
                                                    responseListener = object :
                                                        FetchCoverArt.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            url = data
                                                        }

                                                        override fun onError(message: String) {
                                                            url = ""
                                                            Log.e(
                                                                message,
                                                                "LATEST UPDATED MANGA COVER ART"
                                                            )
                                                        }

                                                    }
                                                )

                                                return@async url
                                            }

                                            val author = async {
                                                lateinit var name: String

                                                FetchAuthorDetail().getAuthorDetail(
                                                    id = getMangaAuthor(mangaResponse),
                                                    responseListener = object :
                                                        FetchAuthorDetail.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            name = data
                                                        }

                                                        override fun onError(message: String) {
                                                            name = ""
                                                            Log.e(
                                                                message,
                                                                "LATEST UPDATED MANGA AUTHOR"
                                                            )
                                                        }

                                                    }
                                                )

                                                return@async name
                                            }

                                            viewModelScope.launch {
                                                loadLatestUpdatedManga(
                                                    newValue =
                                                    MangaModel(
                                                        id = id,
                                                        name = getMangaName(mangaResponse = mangaResponse),
                                                        coverArtUrl = coverArtUrl.await(),
                                                        genre = getMangaTags(mangaResponse = mangaResponse),
                                                        description = getMangaDescription(
                                                            mangaResponse = mangaResponse
                                                        ),
                                                        author = listOf(author.await()),
                                                        status = mangaResponse.data.attributes.status,
                                                        chapters = emptyList()
                                                    )
                                                )
                                            }
                                        }

                                        override fun onError(message: String) {
                                            Log.e(
                                                message,
                                                "LATEST UPDATED MANGA, LOADING...! Not yet implemented"
                                            )
                                            _latestUpdatedMangasError.value = message
                                        }

                                    }
                                )
                            }
                            _isLatestUpdatedMangasLoading.value = false
                        }
                    }

                    override fun onError(message: String) {
                        Log.e(message, "LATEST UPDATED MANGAS IDS! Not yet implemented")
                        _latestUpdatedMangasError.value = message
                    }

                }
            )
        }

        CoroutineScope(IO).launch{
            _isFinishedMangasLoading.value = true

            FetchFinishedManga().getFinishedManga(
                responseListener = object : FetchFinishedManga.ResponseListener {
                    override fun onSuccess(datas: MutableList<String>) {
                        CoroutineScope(IO).launch {
                            for (id in datas) {
                                FetchMangaDetails().getMangaDetail(
                                    id = id,
                                    responseListener = object : FetchMangaDetails.ResponseListener {
                                        override fun onSuccess(mangaResponse: MangaDetailResponse) {

                                            val coverArtUrl = async {
                                                lateinit var url: String

                                                FetchCoverArt().getCoverArt(
                                                    mangaId = mangaResponse.data.id,
                                                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse),
                                                    responseListener = object :
                                                        FetchCoverArt.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            url = data
                                                        }

                                                        override fun onError(message: String) {
                                                            url = ""
                                                            Log.e(
                                                                message,
                                                                "FINISHED MANGA COVER ART"
                                                            )
                                                        }

                                                    }
                                                )

                                                return@async url
                                            }

                                            val author = async {
                                                lateinit var name: String

                                                FetchAuthorDetail().getAuthorDetail(
                                                    id = getMangaAuthor(mangaResponse),
                                                    responseListener = object :
                                                        FetchAuthorDetail.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            name = data
                                                        }

                                                        override fun onError(message: String) {
                                                            name = ""
                                                            Log.e(message, "FINISHED MANGA AUTHOR")
                                                        }

                                                    }
                                                )

                                                return@async name
                                            }

                                            viewModelScope.launch {
                                                loadFinishedManga(
                                                    newValue =
                                                    MangaModel(
                                                        id = id,
                                                        name = getMangaName(mangaResponse = mangaResponse),
                                                        coverArtUrl = coverArtUrl.await(),
                                                        genre = getMangaTags(mangaResponse = mangaResponse),
                                                        description = getMangaDescription(
                                                            mangaResponse = mangaResponse
                                                        ),
                                                        author = listOf(author.await()),
                                                        status = mangaResponse.data.attributes.status,
                                                        chapters = emptyList()
                                                    )
                                                )
                                            }
                                        }

                                        override fun onError(message: String) {
                                            Log.e(
                                                message,
                                                "FINISHED MANGA, LOADING...! Not yet implemented"
                                            )
                                            _finishedMangasError.value = message
                                        }

                                    }
                                )
                            }

                            _isFinishedMangasLoading.value = false
                        }
                    }

                    override fun onError(message: String) {
                        Log.e(message, "FINISHED MANGAS IDS! Not yet implemented")
                        _finishedMangasError.value = message
                    }

                }
            )
        }

        CoroutineScope(IO).launch{
            _isRecentlyAddedMangasLoading.value = true

            FetchRecentlyAddedManga().getRecentlyAddedManga(
                responseListener = object : FetchRecentlyAddedManga.ResponseListener {
                    override fun onSuccess(datas: MutableList<String>) {
                        CoroutineScope(IO).launch {
                            for (id in datas) {
                                FetchMangaDetails().getMangaDetail(
                                    id = id,
                                    responseListener = object : FetchMangaDetails.ResponseListener {
                                        override fun onSuccess(mangaResponse: MangaDetailResponse) {

                                            val coverArtUrl = async {
                                                lateinit var url: String

                                                FetchCoverArt().getCoverArt(
                                                    mangaId = mangaResponse.data.id,
                                                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse),
                                                    responseListener = object :
                                                        FetchCoverArt.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            url = data
                                                        }

                                                        override fun onError(message: String) {
                                                            url = ""
                                                            Log.e(
                                                                message,
                                                                "RECENTLY ADDED MANGA COVER ART"
                                                            )
                                                        }

                                                    }
                                                )

                                                return@async url
                                            }

                                            val author = async {
                                                lateinit var name: String

                                                FetchAuthorDetail().getAuthorDetail(
                                                    id = getMangaAuthor(mangaResponse),
                                                    responseListener = object :
                                                        FetchAuthorDetail.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            name = data
                                                        }

                                                        override fun onError(message: String) {
                                                            name = ""
                                                            Log.e(
                                                                message,
                                                                "RECENTLY ADDED MANGA AUTHOR"
                                                            )
                                                        }

                                                    }
                                                )

                                                return@async name
                                            }

                                            viewModelScope.launch {
                                                loadRecentlyAddedManga(
                                                    newValue =
                                                    MangaModel(
                                                        id = id,
                                                        name = getMangaName(mangaResponse = mangaResponse),
                                                        coverArtUrl = coverArtUrl.await(),
                                                        genre = getMangaTags(mangaResponse = mangaResponse),
                                                        description = getMangaDescription(
                                                            mangaResponse = mangaResponse
                                                        ),
                                                        author = listOf(author.await()),
                                                        status = mangaResponse.data.attributes.status,
                                                        chapters = emptyList()
                                                    )
                                                )
                                            }
                                        }

                                        override fun onError(message: String) {
                                            Log.e(
                                                message,
                                                "RECENTLY ADDED MANGA, LOADING...! Not yet implemented"
                                            )
                                            _recentlyAddedMangasError.value = message
                                        }

                                    }
                                )
                            }

                            _isRecentlyAddedMangasLoading.value = false
                        }
                    }

                    override fun onError(message: String) {
                        Log.e(message, "RECENTLY ADDED MANGAS IDS! Not yet implemented")
                        _recentlyAddedMangasError.value = message
                    }

                }
            )
        }

        CoroutineScope(IO).launch{
            FetchTrendyManga().getTrendyManga(
                responseListener = object : FetchTrendyManga.ResponseListener {
                    override fun onSuccess(datas: MutableList<String>) {
                        CoroutineScope(IO).launch {
                            for (id in datas) {
                                FetchMangaDetails().getMangaDetail(
                                    id = id,
                                    responseListener = object : FetchMangaDetails.ResponseListener {
                                        override fun onSuccess(mangaResponse: MangaDetailResponse) {

                                            val coverArtUrl = async {
                                                lateinit var url: String

                                                FetchCoverArt().getCoverArt(
                                                    mangaId = mangaResponse.data.id,
                                                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse),
                                                    responseListener = object :
                                                        FetchCoverArt.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            url = data
                                                        }

                                                        override fun onError(message: String) {
                                                            url = ""
                                                            Log.e(message, "TRENDY MANGA COVER ART")
                                                        }

                                                    }
                                                )

                                                return@async url
                                            }

                                            val author = async {
                                                lateinit var name: String

                                                FetchAuthorDetail().getAuthorDetail(
                                                    id = getMangaAuthor(mangaResponse),
                                                    responseListener = object :
                                                        FetchAuthorDetail.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            name = data
                                                        }

                                                        override fun onError(message: String) {
                                                            name = ""
                                                            Log.e(message, "TRENDY MANGA AUTHOR")
                                                        }

                                                    }
                                                )

                                                return@async name
                                            }

                                            viewModelScope.launch {
                                                loadTrendyManga(
                                                    newValue = MangaModel(
                                                        id = id,
                                                        name = getMangaName(mangaResponse = mangaResponse),
                                                        coverArtUrl = coverArtUrl.await(),
                                                        genre = getMangaTags(mangaResponse = mangaResponse),
                                                        description = getMangaDescription(
                                                            mangaResponse = mangaResponse
                                                        ),
                                                        author = listOf(author.await()),
                                                        status = mangaResponse.data.attributes.status,
                                                        chapters = emptyList()
                                                    )
                                                )
                                                updateTrendyMangaIndex()
                                            }
                                        }

                                        override fun onError(message: String) {
                                            Log.e(
                                                message,
                                                "TRENDY MANGA, LOADING...! Not yet implemented"
                                            )
                                            _trendyMangasError.value = message
                                        }
                                    }
                                )
                            }
                        }
                    }

                    override fun onError(message: String) {
                        Log.e(message, "TRENDY MANGAS IDS! Not yet implemented")
                        _trendyMangasError.value = message
                    }

                }
            )
        }

        CoroutineScope(IO).launch{
            _isPopularMangasLoading.value = true
            FetchPopularManga().getPopularManga(
                responseListener = object : FetchPopularManga.ResponseListener {
                    override fun onSuccess(datas: MutableList<String>) {
                        CoroutineScope(IO).launch {
                            for (id in datas) {
                                FetchMangaDetails().getMangaDetail(
                                    id = id,
                                    responseListener = object : FetchMangaDetails.ResponseListener {
                                        override fun onSuccess(mangaResponse: MangaDetailResponse) {

                                            val coverArtUrl = async {
                                                lateinit var url: String

                                                FetchCoverArt().getCoverArt(
                                                    mangaId = mangaResponse.data.id,
                                                    coverId = getMangaCoverArtId(mangaResponse = mangaResponse),
                                                    responseListener = object :
                                                        FetchCoverArt.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            url = data
                                                        }

                                                        override fun onError(message: String) {
                                                            url = ""
                                                            Log.e(
                                                                message,
                                                                "POPULAR MANGA COVER ART"
                                                            )
                                                        }

                                                    }
                                                )

                                                return@async url
                                            }

                                            val author = async {
                                                lateinit var name: String

                                                FetchAuthorDetail().getAuthorDetail(
                                                    id = getMangaAuthor(mangaResponse),
                                                    responseListener = object :
                                                        FetchAuthorDetail.ResponseListener {
                                                        override fun onSuccess(data: String) {
                                                            name = data
                                                        }

                                                        override fun onError(message: String) {
                                                            name = ""
                                                            Log.e(message, "POPULAR MANGA AUTHOR")
                                                        }

                                                    }
                                                )

                                                return@async name
                                            }

                                            viewModelScope.launch {
                                                loadPopularManga(
                                                    newValue =
                                                    MangaModel(
                                                        id = id,
                                                        name = getMangaName(mangaResponse = mangaResponse),
                                                        coverArtUrl = coverArtUrl.await(),
                                                        genre = getMangaTags(mangaResponse = mangaResponse),
                                                        description = getMangaDescription(
                                                            mangaResponse = mangaResponse
                                                        ),
                                                        author = listOf(author.await()),
                                                        status = mangaResponse.data.attributes.status,
                                                        chapters = emptyList()
                                                    )
                                                )
                                            }
                                        }

                                        override fun onError(message: String) {
                                            Log.e(
                                                message,
                                                "POPULAR MANGA, LOADING...! Not yet implemented"
                                            )
                                            _popularMangasError.value = message
                                        }

                                    }
                                )
                            }

                            _isPopularMangasLoading.value = false
                        }
                    }

                    override fun onError(message: String) {
                        Log.e(message, "POPULAR MANGAS IDS! Not yet implemented")
                        _popularMangasError.value = message
                    }

                }
            )
        }
    }

    suspend fun fetchSearchedMangas(
        title: String,
        scope: CoroutineScope
    ) {
        FetchSearchedManga().getSearchedManga(
            title = title,
            responseListener = object : FetchSearchedManga.ResponseListener {
                override fun onSuccess(datas: MutableList<String>) {
                    scope.launch {
                        for (id in datas) {
                            FetchMangaDetails().getMangaDetail(
                                id = id,
                                responseListener = object : FetchMangaDetails.ResponseListener {
                                    override fun onSuccess(mangaResponse: MangaDetailResponse) {

                                        val coverArtUrl = async {
                                            lateinit var url: String

                                            FetchCoverArt().getCoverArt(
                                                mangaId = mangaResponse.data.id,
                                                coverId = getMangaCoverArtId(mangaResponse = mangaResponse),
                                                responseListener = object: FetchCoverArt.ResponseListener {
                                                    override fun onSuccess(data: String) {
                                                        url = data
                                                    }

                                                    override fun onError(message: String) {
                                                        url = ""
                                                        Log.e(message,"SEARCHED MANGA COVER ART")
                                                    }

                                                }
                                            )

                                            return@async url
                                        }

                                        val author = async {
                                            lateinit var name: String

                                            FetchAuthorDetail().getAuthorDetail(
                                                id = getMangaAuthor(mangaResponse),
                                                responseListener = object: FetchAuthorDetail.ResponseListener {
                                                    override fun onSuccess(data: String) {
                                                        name = data
                                                    }

                                                    override fun onError(message: String) {
                                                        name = ""
                                                        Log.e(message,"SEARCHED MANGA AUTHOR")
                                                    }

                                                }
                                            )

                                            return@async name
                                        }

                                        CoroutineScope(IO).launch {
                                            loadSearchedManga(newValue = MangaModel(
                                                id = id,
                                                name = com.example.mangareaderui.screens.explorescreen.getMangaName(
                                                    mangaResponse = mangaResponse
                                                ),
                                                coverArtUrl = coverArtUrl.await(),
                                                genre = com.example.mangareaderui.screens.explorescreen.getMangaTags(
                                                    mangaResponse = mangaResponse
                                                ),
                                                description = com.example.mangareaderui.screens.explorescreen.getMangaDescription(
                                                    mangaResponse = mangaResponse
                                                ),
                                                author = listOf(author.await()),
                                                status = mangaResponse.data.attributes.status,
                                                chapters = null
                                            ))
                                            updateSearchedMangaIndex()
                                            updateIsSearchingState(false)
                                        }
                                    }

                                    override fun onError(message: String) {
                                        Log.e(message,"SEARCHED MANGA, LOADING...! Not yet implemented")
                                        _searchedMangasError.value = message
                                    }

                                }
                            )
                        }
                    }
                }

                override fun onError(message: String) {
                    Log.e(message,"SEARCHED MANGAS IDS! Not yet implemented")
                    _searchedMangasError.value = message
                }

            }
        )
    }

    suspend fun fetchChapterListData(
        mangaId: String,
        scope: CoroutineScope
    ) {
        _chapterListError.value = ""
        clearChapterList()

        FetchChapterList().getChapterList(
            id = mangaId,
            responseListener = object: FetchChapterList.ResponseListener {
                override fun onSuccess(datas: MutableList<String>) {
                    scope.launch {
                        for (chapterId in datas) {
                            FetchChapterDetail().getChapterDetail(
                                id = chapterId,
                                responseListener = object: FetchChapterDetail.ResponseListener {
                                    override fun onSuccess(data: ChapterDetailResponse) {

                                        CoroutineScope(IO).launch {
                                            val chapterData = ChapterModel(
                                                id = chapterId,
                                                title = data.data.attributes.title ?: "",
                                                chapterPagesImageUrls = emptyList(),
                                                chapter = data.data.attributes.chapter ?: "",
                                                pages = data.data.attributes.pages ?: 0,
                                                date = data.data.attributes.updatedAt ?: "",
                                                language = data.data.attributes.translatedLanguage ?: ""
                                            )

                                            Log.e("CHAPTER_DATA", chapterData.toString())
                                            loadChapterList(newValue = chapterData)
                                        }
                                    }

                                    override fun onError(message: String) {
                                        Log.e(message,"CHAPTER DETAIL! Not yet implemented")
                                    }

                                }
                            )
                        }
                        Log.e("CHAPTER_DATA_LIST_SIZE", _chapterList.value.size.toString())
                    }
                }

                override fun onError(message: String) {
                    Log.e(message,"CHAPTER LIST IDS! Not yet implemented")
                    loadChapterListError(newValue = message)
                }

            }
        )
    }

    suspend fun fetchChapterPagesData(
        chapterId: String
    ) {
        FetchChapterData().getChapterData(
            id = chapterId,
            responseListener = object: FetchChapterData.ResponseListener {
                override fun onSuccess(datas: MutableList<String>) {
                    loadChapterLinks(newValue = datas)
                }

                override fun onError(message: String) {
                    loadChapterLinks(newValue = listOf(""))
                    Log.e(message,"CHAPTER PAGE IMAGE URLS")
                }

            }
        )
    }


    init {
        fetchData()
    }
}