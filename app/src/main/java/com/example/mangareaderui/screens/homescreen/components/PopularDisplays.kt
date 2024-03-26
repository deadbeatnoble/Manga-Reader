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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.R
import com.example.mangareaderui.domain.model.MangaModel
import com.example.mangareaderui.screens.mainscreen.MainScreenNavGraph

/*@Composable
fun PopularDisplays() {
    val mangaManhwaManhua = listOf(
        "Manga",
        "Manhwa",
        "Manhua"
    )
    val activeMiniTab = listOf(
        "manga",
        "manhwa",
        "manhua"
    )
    var active by remember {
        mutableStateOf(activeMiniTab[0])
    }
    var content by remember {
        mutableStateOf(mangaManhwaManhua[0])
    }
    Column {
        Text(
            text = "Popular",
            color = colorResource(id = R.color.text),
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(percent = 25))
                    .background(color = colorResource(id = R.color.light_background)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Manga",
                    color = if (active == activeMiniTab[0]) Color.White else Color.Black,
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 25))
                        .background(color = if (active == activeMiniTab[0]) colorResource(id = R.color.text) else Color.Transparent)
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clickable {
                            content = mangaManhwaManhua[0]
                            active = activeMiniTab[0]
                        }
                )
                Text(
                    text = "Manhwa",
                    color = if (active == activeMiniTab[1]) Color.White else Color.Black,
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 25))
                        .background(color = if (active == activeMiniTab[1]) colorResource(id = R.color.text) else Color.Transparent)
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clickable {
                            content = mangaManhwaManhua[1]
                            active = activeMiniTab[1]
                        }
                )
                Text(
                    text = "Manhua",
                    color = if (active == activeMiniTab[2]) Color.White else Color.Black,
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 25))
                        .background(color = if (active == activeMiniTab[2]) colorResource(id = R.color.text) else Color.Transparent)
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clickable {
                            content = mangaManhwaManhua[2]
                            active = activeMiniTab[2]
                        }
                )
            }
        }
        repeat(3) {index ->
            val times = index+1
            val rank = times.toString()
            val icon = if (times == 1) R.drawable.shield_filled else R.drawable.shield_outline
            val textColor = if (times == 1) R.color.white else R.color.black
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp),
                        tint = colorResource(id = R.color.text)
                    )
                    Text(
                        text = rank,
                        color = colorResource(id = textColor),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )

                }
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.img1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = content,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(R.color.light_grey)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Solo Leveling",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp),
                            tint = colorResource(id = R.color.text)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "400 K",
                            color = colorResource(id = R.color.text),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}*/

@Composable
fun PopularDisplay(
    mangas: List<MangaModel>,
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Popular",
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            repeat(2) { rowTimes ->
                Column {
                    repeat(3) { columnTimes ->
                        val times = if (rowTimes == 0) rowTimes +1 * columnTimes +1 else (columnTimes +1 * rowTimes +1) +rowTimes+1

                        val manga = mangas[times-1]

                        if (manga.id != null && manga.name != null) {
                            SinglePopularMangaDisplay(manga = manga, times = times, mainViewModel = mainViewModel, navController = navController)
                        } else {
                            SinglePopularMangaDisplayShimmerEffect()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SinglePopularMangaDisplay(
    manga: MangaModel,
    times: Int,
    mainViewModel: MainViewModel,
    navController: NavHostController
) {

    val rank = times.toString()
    val icon = if (times == 1) R.drawable.shield_filled else R.drawable.shield_outline
    val textColor = if (times == 1) R.color.white else R.color.black
    val status = if(manga.status!! == "ongoing") Color.Green else Color.Red

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
        builder = { }
    )
    Row(
        modifier = Modifier
            .padding(8.dp)
            .height(130.dp)
            .width(300.dp)
            .clickable {
                mainViewModel.clearChapterList()
                mainViewModel.loadMangaDetail(newValue = manga)
                navController.navigate(MainScreenNavGraph.DetailGraph.route)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp),
                tint = colorResource(id = R.color.text)
            )
            Text(
                text = rank,
                color = colorResource(id = textColor),
                modifier = Modifier
                    .align(Alignment.Center)
            )

        }
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(80.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush = brush)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = manga.name!!,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.status),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp),
                    tint = status
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = manga.status!!,
                    color = colorResource(id = R.color.text),
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun SinglePopularMangaDisplayShimmerEffect() {

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

    Row(
        modifier = Modifier
            .padding(8.dp)
            .height(130.dp)
            .width(300.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush = brush)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush = brush)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush = brush)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush = brush)
                )
            }
        }
    }
}

@Preview
@Composable
fun PopularDisplaysPreview() {
    //PopularDisplay(mangas = emptyList(), mainViewModel = MainViewModel(), navController = rememberNavController())
    //PopularDisplayShimmerEffect()
    //SinglePopularMangaDisplay(MangaModel(null,null,null, emptyList(),null, emptyList(),null, emptyList()), 2)
    SinglePopularMangaDisplayShimmerEffect()
}