package com.example.mangareaderui.screens.explorescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.R
import com.example.mangareaderui.domain.model.MangaModel
import com.example.mangareaderui.network.requests.FetchAuthorDetail
import com.example.mangareaderui.network.requests.FetchCoverArt
import com.example.mangareaderui.network.requests.FetchMangaDetails
import com.example.mangareaderui.network.requests.FetchSearchedManga
import com.example.mangareaderui.screens.explorescreen.components.SearchedResultDisplay
import com.example.mangareaderui.screens.homescreen.HomeAppBar
import com.example.retrofit.network.model.mangadetail.MangaDetailResponse
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val searchWidgetState by mainViewModel.searchWidgetUiState
    val searchTextState by mainViewModel.searchTextUiState
    val isSearchingState by mainViewModel.isSearchingState

    val searchedMangas: MutableList<MangaModel> = mainViewModel.searchedManga.value

    val scope = rememberCoroutineScope()

    if (isSearchingState) {
        LaunchedEffect(true) {
            val searchedIds = FetchSearchedManga().getSearchedManga(searchTextState.trim())

            scope.launch {
                for (id in searchedIds) {
                    val mangaResponse = FetchMangaDetails().getMangaDetail(id = id)

                    val coverArtUrl = FetchCoverArt().getCoverArt(
                        mangaId = mangaResponse.data.id,
                        coverId = getMangaCoverArtId(mangaResponse = mangaResponse)
                    )

                    val author = FetchAuthorDetail().getAuthorDetail(
                        id = getMangaAuthor(mangaResponse)
                    )

                    mainViewModel.loadSearchedManga(newValue = MangaModel(
                        id = id,
                        name = getMangaName(mangaResponse = mangaResponse),
                        coverArtUrl = coverArtUrl,
                        genre = getMangaTags(mangaResponse = mangaResponse),
                        description = getMangaDescription(mangaResponse = mangaResponse),
                        author = listOf(author),
                        status = mangaResponse.data.attributes.status,
                        chapters = null
                    ))
                    mainViewModel.updateSearchedMangaIndex()
                    mainViewModel.updateIsSearchingState(false)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ExploreAppBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    mainViewModel.updateSearchTextState(newValue = it)
                },
                onCancelClicked = {
                    scope.coroutineContext.cancelChildren()
                    mainViewModel.updateSearchTextState(newValue = "")
                    mainViewModel.updateIsSearchingState(false)
                    mainViewModel.clearSearchedManga()
                    mainViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    scope.coroutineContext.cancelChildren()
                    mainViewModel.clearSearchedManga()
                    mainViewModel.updateIsSearchingState(true)
                                  },
                onSearchTriggered = {
                    mainViewModel.updateSearchWidgetState(SearchWidgetState.OPENED)
                }
            )
        }
    ) {
        if (searchWidgetState == SearchWidgetState.CLOSED) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "explore")
            }
        } else {
            SearchedResultDisplay(mainViewModel = mainViewModel, navController = navController, mangas = searchedMangas)
        }
    }

}

@Composable
fun ExploreAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    when(searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            HomeAppBar(onSearchClicked = onSearchTriggered)
        }
        SearchWidgetState.OPENED -> {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
            }
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCancelClicked = onCancelClicked,
                onSearchClicked = onSearchClicked,
                focusRequester = focusRequester
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    focusRequester: FocusRequester
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = Color.White
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = "Search here...",
                    color = Color.Black
                )
            },
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = { onSearchClicked(text) },
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = colorResource(id = R.color.text)
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCancelClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            )
        )
    }
}


fun getMangaAuthor(mangaResponse: MangaDetailResponse): String {
    lateinit var author: String

    for (relationship in mangaResponse.data.relationships) {
        when(relationship.type) {
            "author" -> author = relationship.id
            else -> continue
        }
    }

    return author
}

fun getMangaDescription(mangaResponse: MangaDetailResponse): String {
    val description = if (!mangaResponse.data.attributes.description.en.isNullOrEmpty()) {
        mangaResponse.data.attributes.description.en
    } else if (mangaResponse.data.attributes.description.en.isNullOrEmpty() && !mangaResponse.data.attributes.description.ja.isNullOrEmpty()) {
        mangaResponse.data.attributes.description.ja
    } else {
        "<No Description!>"
    }

    return description
}

fun getMangaTags(mangaResponse: MangaDetailResponse): List<String> {
    val genre: MutableList<String> = mutableListOf()

    for (tag in mangaResponse.data.attributes.tags) {
        when(tag.attributes.group) {
            "genre" -> genre.add(tag.attributes.name.en)
            else -> continue
        }
    }

    return if (genre.isEmpty()) listOf("") else genre
}

fun getMangaCoverArtId(mangaResponse: MangaDetailResponse): String {
    lateinit var mangaCoverArtId: String

    for (relationship in mangaResponse.data.relationships) {
        when(relationship.type) {
            "cover_art" -> mangaCoverArtId = relationship.id
            else -> continue
        }
    }

    return mangaCoverArtId

}

fun getMangaName(mangaResponse: MangaDetailResponse): String {
    lateinit var mangaName: String

    if (!mangaResponse.data.attributes.title.en.isNullOrEmpty()) {
        mangaName = mangaResponse.data.attributes.title.en
    } else if (!mangaResponse.data.attributes.title.ja.isNullOrEmpty()) {
        mangaName = mangaResponse.data.attributes.title.ja
    } else {
        for (lang in mangaResponse.data.attributes.altTitles) {
            if (!lang.en.isNullOrEmpty()) {
                mangaName = lang.en
                break
            } else if (lang.en.isNullOrEmpty() && !lang.ja.isNullOrEmpty()) {
                mangaName = lang.ja
            } else if (lang.en.isNullOrEmpty() && lang.ja.isNullOrEmpty() && !lang.ko.isNullOrEmpty()) {
                mangaName = lang.ko
            }
        }
    }

    return mangaName
}


@Preview
@Composable
fun ExploreScreenPreview() {
    ExploreScreen(mainViewModel = MainViewModel(), navController = rememberNavController())
}