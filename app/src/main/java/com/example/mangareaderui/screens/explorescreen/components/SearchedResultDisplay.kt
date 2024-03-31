package com.example.mangareaderui.screens.explorescreen.components

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.R
import com.example.mangareaderui.domain.model.MangaModel
import com.example.mangareaderui.screens.mainscreen.MainScreenNavGraph

@Composable
fun SearchedResultDisplay(
    searchedMangasError: String,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    mangas: List<MangaModel>
) {
    LazyColumn(contentPadding = PaddingValues(vertical = 50.dp)) {
        item {
            Text(
                text = "-- Found a total of ${mangas.size} results --",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        if (searchedMangasError.isEmpty()) {
            items(mangas) { manga ->
                if (manga.id != null && manga.name != null && manga.coverArtUrl != null) {
                    SingleSearchedResult(mainViewModel = mainViewModel, navController = navController, manga = manga)
                } else {
                    SingleSearchedResultShimmerEffect()
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            item{ Text(text = searchedMangasError, color = Color.Red) }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleSearchedResult(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    manga: MangaModel
) {

    val painter = rememberImagePainter(
        data = manga.coverArtUrl!!,
        builder = {

        }
    )
    Surface(
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                mainViewModel.clearChapterList()
                mainViewModel.loadMangaDetail(newValue = manga)
                navController.navigate(MainScreenNavGraph.DetailGraph.route)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(75.dp),
                contentScale = ContentScale.Crop

            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = manga.name!!,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.text),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = "Chapter ",
                    fontWeight = FontWeight.Light,
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.light_blue),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = "2024-03-18",
                    fontWeight = FontWeight.Thin,
                    fontSize = 15.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SingleSearchedResultShimmerEffect() {
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
    Surface(
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(75.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush = brush)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush = brush)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush = brush)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .height(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush = brush)
                )
            }
        }
    }
}

@Preview
@Composable
fun SingleSearchedResultPreview() {
    SingleSearchedResult(mainViewModel = MainViewModel(), navController = rememberNavController(), manga = MangaModel("","Solo Leveling: only i am the one that levels up!", "", emptyList(), "", emptyList(), "", emptyList()))
}