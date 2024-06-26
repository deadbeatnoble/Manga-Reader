package com.example.mangareaderui.screens.homescreen.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.R
import com.example.mangareaderui.domain.model.MangaModel
import com.example.mangareaderui.screens.mainscreen.MainScreenNavGraph

@Composable
fun FinishedDisplays(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val finishedMangas by mainViewModel.finishedManga.collectAsState()
    val isLoading by mainViewModel.isFinishedMangasLoading.collectAsState()
    val finishedMangasError by mainViewModel.finishedMangasError.collectAsState()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Finished",
                color = colorResource(id = R.color.text),
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "see more",
                    color = Color(R.color.light_grey)
                )
                Icon(
                    painter = painterResource(id = R.drawable.see_more),
                    contentDescription = null,
                    modifier = Modifier
                        .size(15.dp),
                    tint = Color(R.color.light_grey)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Color.LightGray)
        )
        LazyRow {
            if (!isLoading) {
                if(finishedMangasError.isEmpty()) {
                    items(finishedMangas) { manga ->
                        SingleFinishedDisplays(
                            manga = manga,
                            mainViewModel = mainViewModel,
                            navController = navController,
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 16.dp, start = 16.dp)
                                .width(125.dp)
                        )
                    }
                } else {
                    item{ Text(text = finishedMangasError, color = Color.Red) }
                }
            } else {
                if (finishedMangasError.isEmpty()) {
                    item {
                        repeat(10) {
                            SingleFinishedShimmerEffectDisplays(modifier = Modifier
                                .padding(top = 8.dp, bottom = 16.dp, start = 16.dp)
                                .width(125.dp))
                        }
                    }
                } else {
                    item{ Text(text = finishedMangasError, color = Color.Red) }
                }
            }
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleFinishedDisplays(
    manga: MangaModel,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    modifier: Modifier
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
        data = manga.coverArtUrl!!,
        builder = {

        }
    )
    Column (
        modifier = modifier
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush)
                .clickable{
                    mainViewModel.clearChapterList()
                    mainViewModel.loadMangaDetail(newValue = manga)
                    navController.navigate(MainScreenNavGraph.DetailGraph.route)
                },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = manga.name!!,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun SingleFinishedShimmerEffectDisplays(
    modifier: Modifier
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
    Column (
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(25.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(brush)
        )
    }
}



@Preview
@Composable
fun FinishedDisplaysPreview() {

}