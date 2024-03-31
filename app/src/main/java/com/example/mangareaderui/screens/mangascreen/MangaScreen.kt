package com.example.mangareaderui.screens.mangascreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.R
import com.example.mangareaderui.screens.mangascreen.components.ChapterListDisplay
import com.example.mangareaderui.screens.mangascreen.components.DescriptionDisplay
import com.example.mangareaderui.screens.mangascreen.components.GenreListDisplay
import com.example.mangareaderui.screens.mangascreen.components.MangaDetailDisplay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MangaScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()

    val manga = mainViewModel.mangaDetail.value
    val chapterList by remember {
        mutableStateOf(mainViewModel.chapterList.value)
    }
    var chapterListError by remember {
        mutableStateOf(mainViewModel.chapterListError.value)
    }

    LaunchedEffect(true) {
        mainViewModel.fetchChapterListData(mangaId = manga.id!!)
        Log.e("CHAPTER_DATA_LIST", mainViewModel.chapterList.value.size.toString())
    }

    if (manga.id != null && manga.name != null && manga.coverArtUrl != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    MangaDetailDisplay(manga = manga, scope = scope, navController = navController)
                }
                item {
                    if (manga.genre != null) {
                        GenreListDisplay(genres = manga.genre, modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
                item {
                    DescriptionDisplay(description = manga.description ?: "<No Description!>", modifier = Modifier.padding(horizontal = 16.dp))
                }
                if (chapterList.isNotEmpty()) {
                    items(chapterList) { chapter ->
                        ChapterListDisplay(
                            mainViewModel = mainViewModel,
                            navController = navController,
                            chapter = chapter,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                } else if (mainViewModel.chapterListError.value.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Text(text = mainViewModel.chapterListError.value)
                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "Retry")
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        MangaScreenShimmerEffect(scope = scope, navController = navController)
    }
}

@Composable
fun MangaScreenShimmerEffect(
    scope: CoroutineScope,
    navController: NavHostController
) {

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )
    val transition = rememberInfiniteTransition()
    val transitionAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = transitionAnimation.value, y = transitionAnimation.value)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(25.dp)
                                .padding(horizontal = 16.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    scope.coroutineContext.cancelChildren()
                                    navController.popBackStack()
                                },
                                modifier = Modifier
                                    .background(Color.Transparent)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .size(15.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(130.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(brush = brush)
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(25.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(brush = brush)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .height(25.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(brush = brush)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height(25.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(brush = brush)
                                )
                            }
                        }
                    }
                }
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(5) {
                        Box(
                            modifier = Modifier
                                .height(25.dp)
                                .width(70.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(colorResource(id = R.color.light_grey))
                                .padding(6.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush = brush)
                )
            }
            items(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(8.dp)
                        .background(brush = brush)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
fun MangaScreenPreview() {
    MangaScreen(navController = rememberNavController(), mainViewModel = MainViewModel())
}