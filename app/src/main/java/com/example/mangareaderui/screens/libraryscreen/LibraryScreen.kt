package com.example.mangareaderui.screens.libraryscreen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.R
import com.example.mangareaderui.domain.data.BookmarkViewModel
import com.example.mangareaderui.domain.model.MangaModel
import com.example.mangareaderui.screens.mainscreen.MainScreenNavGraph

@Composable
fun LibraryScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {

    val context = LocalViewModelStoreOwner.current
    val bookmarkViewModel: BookmarkViewModel = ViewModelProvider(context!!).get(BookmarkViewModel::class.java)

    val bookmarkedMangas by bookmarkViewModel.bookmarkedMangas.collectAsState()

    LaunchedEffect(true) {
        bookmarkViewModel.loadBookmarks()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(bookmarkedMangas) { manga ->
                BookmarkedMangaDisplay(
                    mangaModel = manga,
                    navController = navController,
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun BookmarkedMangaDisplay(
    mangaModel: MangaModel,
    navController: NavHostController,
    mainViewModel: MainViewModel
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
    val painter = rememberImagePainter(
        data = mangaModel.coverArtUrl!!,
        builder = {

        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(8.dp)
            .clickable {
                mainViewModel.clearChapterList()
                mainViewModel.loadMangaDetail(newValue = mangaModel)
                navController.navigate(MainScreenNavGraph.DetailGraph.route)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(175.dp)
                .width(125.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = mangaModel.name!!,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mangaModel.author.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.bookmark_filled),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Top)
        )
    }
}