package com.example.mangareaderui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangareaderui.domain.model.ChapterModel
import com.example.mangareaderui.domain.model.MangaModel
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
        value = MutableList<MangaModel>(10) {
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
        value = MutableList<MangaModel>(10) {
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
        value = MutableList<MangaModel>(10) {
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
    private val _chapterList: State<MutableList<ChapterModel>> =
        mutableStateOf(value = mutableStateListOf())
    val chapterList: State<List<ChapterModel>> = _chapterList

    private val _chapterListError: MutableState<String> =
        mutableStateOf(value = "")
    val chapterListError: State<String> = _chapterListError

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

    fun loadChapterList(newValue: List<ChapterModel>) {
        _chapterList.value.clear()
        _chapterList.value.addAll(newValue)
    }

    fun loadChapterListError(newValue: String) {
        _chapterListError.value = newValue
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

    fun updateTrendyMangaIndex() {
        _trendyMangaIndex.value++
    }
    fun updateSearchedMangaIndex() {
        _searchedMangaIndex.value++
    }

    fun updateSearchedMangaError() {
        _searchedMangasError.value = ""
    }

    suspend fun fetchData() {
        _isLatestUpdatedMangasLoading.value = true
        _isFinishedMangasLoading.value = true
        _isRecentlyAddedMangasLoading.value = true
        _isPopularMangasLoading.value = true

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
                                            FetchCoverArt().getCoverArt(
                                                mangaId = mangaResponse.data.id,
                                                coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                                            )
                                        }

                                        val author = async {
                                            FetchAuthorDetail().getAuthorDetail(
                                                id = getMangaAuthor(mangaResponse)
                                            )
                                        }

                                        viewModelScope.launch {
                                            loadLatestUpdatedManga(newValue =
                                            MangaModel(
                                                id = id,
                                                name = getMangaName(mangaResponse = mangaResponse),
                                                coverArtUrl = coverArtUrl.await(),
                                                genre = getMangaTags(mangaResponse = mangaResponse),
                                                description = getMangaDescription(mangaResponse = mangaResponse),
                                                author = listOf(author.await()),
                                                status = mangaResponse.data.attributes.status,
                                                chapters = emptyList()
                                            )
                                            )
                                        }
                                    }

                                    override fun onError(message: String) {
                                        Log.e(message,"LOADING! Not yet implemented")
                                        _latestUpdatedMangasError.value = message
                                    }

                                }
                            )
                        }
                        _isLatestUpdatedMangasLoading.value = false
                    }
                }

                override fun onError(message: String) {
                    Log.e(message,"Not yet implemented")
                    _latestUpdatedMangasError.value = message
                }

            }
        )
        FetchFinishedManga().getFinishedManga(
            responseListener = object: FetchFinishedManga.ResponseListener {
                override fun onSuccess(datas: MutableList<String>) {
                    CoroutineScope(IO).launch {
                        for (id in datas) {
                            FetchMangaDetails().getMangaDetail(
                                id = id,
                                responseListener = object : FetchMangaDetails.ResponseListener {
                                    override fun onSuccess(mangaResponse: MangaDetailResponse) {

                                        val coverArtUrl = async {
                                            FetchCoverArt().getCoverArt(
                                                mangaId = mangaResponse.data.id,
                                                coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                                            )
                                        }

                                        val author = async {
                                            FetchAuthorDetail().getAuthorDetail(
                                                id = getMangaAuthor(mangaResponse)
                                            )
                                        }

                                        viewModelScope.launch {
                                            loadFinishedManga(newValue =
                                            MangaModel(
                                                id = id,
                                                name = getMangaName(mangaResponse = mangaResponse),
                                                coverArtUrl = coverArtUrl.await(),
                                                genre = getMangaTags(mangaResponse = mangaResponse),
                                                description = getMangaDescription(mangaResponse = mangaResponse),
                                                author = listOf(author.await()),
                                                status = mangaResponse.data.attributes.status,
                                                chapters = emptyList()
                                            )
                                            )
                                        }
                                    }

                                    override fun onError(message: String) {
                                        Log.e(message,"LOADING! Not yet implemented")
                                        _finishedMangasError.value = message
                                    }

                                }
                            )
                        }

                        _isFinishedMangasLoading.value = false
                    }
                }

                override fun onError(message: String) {
                    Log.e(message,"Not yet implemented")
                    _finishedMangasError.value = message
                }

            }
        )
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
                                            FetchCoverArt().getCoverArt(
                                                mangaId = mangaResponse.data.id,
                                                coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                                            )
                                        }

                                        val author = async {
                                            FetchAuthorDetail().getAuthorDetail(
                                                id = getMangaAuthor(mangaResponse)
                                            )
                                        }

                                        viewModelScope.launch {
                                            loadRecentlyAddedManga(newValue =
                                            MangaModel(
                                                id = id,
                                                name = getMangaName(mangaResponse = mangaResponse),
                                                coverArtUrl = coverArtUrl.await(),
                                                genre = getMangaTags(mangaResponse = mangaResponse),
                                                description = getMangaDescription(mangaResponse = mangaResponse),
                                                author = listOf(author.await()),
                                                status = mangaResponse.data.attributes.status,
                                                chapters = emptyList()
                                            )
                                            )
                                        }
                                    }

                                    override fun onError(message: String) {
                                        Log.e(message,"LOADING! Not yet implemented")
                                        _recentlyAddedMangasError.value = message
                                    }

                                }
                            )
                        }

                        _isRecentlyAddedMangasLoading.value = false
                    }
                }

                override fun onError(message: String) {
                    Log.e(message,"Not yet implemented")
                    _recentlyAddedMangasError.value = message
                }

            }
        )
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
                                            FetchCoverArt().getCoverArt(
                                                mangaId = mangaResponse.data.id,
                                                coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                                            )
                                        }

                                        val author = async {
                                            FetchAuthorDetail().getAuthorDetail(
                                                id = getMangaAuthor(mangaResponse)
                                            )
                                        }

                                        viewModelScope.launch {
                                            loadTrendyManga(newValue = MangaModel(
                                                id = id,
                                                name = getMangaName(mangaResponse = mangaResponse),
                                                coverArtUrl = coverArtUrl.await(),
                                                genre = getMangaTags(mangaResponse = mangaResponse),
                                                description = getMangaDescription(mangaResponse = mangaResponse),
                                                author = listOf(author.await()),
                                                status = mangaResponse.data.attributes.status,
                                                chapters = emptyList()
                                            ))
                                            updateTrendyMangaIndex()
                                        }
                                    }

                                    override fun onError(message: String) {
                                        Log.e(message,"LOADING! Not yet implemented")
                                        _trendyMangasError.value = message
                                    }
                                }
                            )
                        }
                    }
                }

                override fun onError(message: String) {
                    Log.e(message,"Not yet implemented")
                    _trendyMangasError.value = message
                }

            }
        )
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
                                            FetchCoverArt().getCoverArt(
                                                mangaId = mangaResponse.data.id,
                                                coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                                            )
                                        }

                                        val author = async {
                                            FetchAuthorDetail().getAuthorDetail(
                                                id = getMangaAuthor(mangaResponse)
                                            )
                                        }

                                        viewModelScope.launch {
                                            loadPopularManga(newValue =
                                            MangaModel(
                                                id = id,
                                                name = getMangaName(mangaResponse = mangaResponse),
                                                coverArtUrl = coverArtUrl.await(),
                                                genre = getMangaTags(mangaResponse = mangaResponse),
                                                description = getMangaDescription(mangaResponse = mangaResponse),
                                                author = listOf(author.await()),
                                                status = mangaResponse.data.attributes.status,
                                                chapters = emptyList()
                                            )
                                            )
                                        }
                                    }

                                    override fun onError(message: String) {
                                        Log.e(message,"LOADING! Not yet implemented")
                                        _popularMangasError.value = message
                                    }

                                }
                            )
                        }

                        _isPopularMangasLoading.value = false
                    }
                }

                override fun onError(message: String) {
                    Log.e(message,"Not yet implemented")
                    _popularMangasError.value = message
                }

            }
        )
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
                                            FetchCoverArt().getCoverArt(
                                                mangaId = mangaResponse.data.id,
                                                coverId = com.example.mangareaderui.screens.explorescreen.getMangaCoverArtId(
                                                    mangaResponse = mangaResponse
                                                )
                                            )
                                        }

                                        val author = async {
                                            FetchAuthorDetail().getAuthorDetail(
                                                id = com.example.mangareaderui.screens.explorescreen.getMangaAuthor(
                                                    mangaResponse
                                                )
                                            )
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
                                        Log.e(message,"LOADING! Not yet implemented")
                                        _searchedMangasError.value = message
                                    }

                                }
                            )
                        }
                    }
                }

                override fun onError(message: String) {
                    Log.e(message,"Not yet implemented")
                    _searchedMangasError.value = message
                }

            }
        )
    }

    suspend fun fetchChapterListData(
        mangaId: String
    ) {
        _chapterListError.value = ""
        FetchChapterList().getChapterList(
            id = mangaId,
            responseListener = object: FetchChapterList.ResponseListener {
                override fun onSuccess(datas: MutableList<String>) {
                    CoroutineScope(IO).launch {
                        val chapterDataList = mutableListOf<ChapterModel>()

                        for (chapterId in datas) {
                            val chapterData = FetchChapterDetail().getChapterDetail(id = chapterId)

                            chapterDataList.add(
                                ChapterModel(
                                    id = chapterId,
                                    title = chapterData.data.attributes.title ?: "",
                                    chapterPagesImageUrls = FetchChapterData().getChapterData(chapterId),
                                    chapter = chapterData.data.attributes.chapter ?: "",
                                    pages = chapterData.data.attributes.pages ?: 0,
                                    date = chapterData.data.attributes.updatedAt ?: ""
                                )
                            )

                            Log.e("CHAPTER_DATA", ChapterModel(id = chapterId, title = chapterData.data.attributes.title ?: "", chapterPagesImageUrls = FetchChapterData().getChapterData(chapterId), chapter = chapterData.data.attributes.chapter ?: "", pages = chapterData.data.attributes.pages ?: 0, date = chapterData.data.attributes.updatedAt ?: "").toString())
                        }

                        Log.e("CHAPTER_DATA_LIST_SIZE", chapterDataList.size.toString())
                        loadChapterList(newValue = chapterDataList)
                    }
                }

                override fun onError(message: String) {
                    loadChapterListError(newValue = message)
                }

            }
        )
    }
}