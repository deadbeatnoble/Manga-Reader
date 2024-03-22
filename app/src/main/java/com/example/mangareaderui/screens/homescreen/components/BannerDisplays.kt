package com.example.mangareaderui.screens.homescreen.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.BlurTransformation
import coil.transform.GrayscaleTransformation
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.domain.model.MangaModel
import com.example.mangareaderui.screens.mainscreen.MainScreenNavGraph
import kotlinx.coroutines.delay

@Composable
fun BannerDisplays(
    mangas: List<MangaModel>,
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val currentIndex = remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            currentIndex.value = if (mangas[currentIndex.value].id != null) (currentIndex.value + 1) % mangas.size else currentIndex.value
        }
    }

    if (mangas[currentIndex.value].id != null) {
        BannerLayout(
            mangas = mangas,
            currentIndex = currentIndex,
            navController = navController,
            mainViewModel = mainViewModel
        )
    } else {
        BannerDisplaysShimmerEffect()
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun BannerLayout(
    mangas: List<MangaModel>,
    currentIndex: MutableState<Int>,
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val painter = rememberImagePainter(
        data = mangas[currentIndex.value].coverArtUrl,
        builder = {

        }
    )
    val bgPainter = rememberImagePainter(
        data = mangas[currentIndex.value].coverArtUrl,
        builder = {
            transformations(
                GrayscaleTransformation(),
                BlurTransformation(LocalContext.current, 20f)
            )
        }
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp)
    ) {
        Image(
            painter = bgPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        ),
                        startY = size.height / 10,
                        endY = size.height
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient,blendMode = BlendMode.Multiply)
                    }
                }
                .clickable {
                    mainViewModel.clearChapterList()
                    mainViewModel.loadMangaDetail(newValue = mangas[currentIndex.value])
                    navController.navigate(MainScreenNavGraph.DetailGraph.route)
                }
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 35.dp, bottom = 16.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(125.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = mangas[currentIndex.value].name!!,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mangas[currentIndex.value].description!!,
                    maxLines = 4,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BannerDisplaysShimmerEffect() {
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
            .fillMaxWidth()
            .height(225.dp)
            .padding(start = 16.dp, end = 16.dp, top = 35.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(125.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun BannerDisplaysPreview() {
    BannerDisplaysShimmerEffect()
}