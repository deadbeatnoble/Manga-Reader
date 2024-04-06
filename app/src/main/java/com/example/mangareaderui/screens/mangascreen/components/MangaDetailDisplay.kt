package com.example.mangareaderui.screens.mangascreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.BlurTransformation
import coil.transform.GrayscaleTransformation
import com.example.mangareaderui.R
import com.example.mangareaderui.domain.model.MangaModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MangaDetailDisplay(
    manga: MangaModel,
    scope: CoroutineScope,
    navController: NavHostController,
    addToLibrary: () -> Unit,
    isBookmarked: Boolean
) {

    val painter = rememberImagePainter(
        data = manga.coverArtUrl,
        builder = {

        }
    )

    val bgPainter = rememberImagePainter(
        data = manga.coverArtUrl,
        builder = {
            transformations(
                GrayscaleTransformation(),
                BlurTransformation(LocalContext.current, 20f)
            )
        }
    )

    val icon = if (isBookmarked) R.drawable.bookmark_filled else R.drawable.bookmark_outline

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
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
                        drawRect(gradient, blendMode = BlendMode.Multiply)
                    }
                }
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
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
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
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .size(15.dp)
                    )
                }

                IconButton(
                    onClick = addToLibrary,
                    modifier = Modifier
                        .background(Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = colorResource(id = R.color.text),
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
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(130.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = manga.name ?: "<No Name!>",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = manga.author?.first() ?: "<No Author!>",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.LightGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = manga.status ?: "",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (manga.status == "ongoing") Color.Green else Color.Red,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}