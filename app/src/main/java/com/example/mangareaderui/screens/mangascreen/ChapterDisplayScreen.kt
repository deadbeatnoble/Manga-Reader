package com.example.mangareaderui.screens.mangascreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.example.mangareaderui.MainViewModel

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ChapterDisplayScreen(
    mainViewModel: MainViewModel
) {
    val chapterPageImages = mainViewModel.chapterLinks.value

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        var scale by remember {
            mutableStateOf(1f)
        }
        var offset by remember {
            mutableStateOf(Offset.Zero)
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio((mainViewModel.screenWidth.value.toFloat() / mainViewModel.screenHeight.value.toFloat()))
        ) {
            val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
                scale = (scale * zoomChange).coerceIn(1f, 5f)

                val extraWidth = (scale - 1) * constraints.maxWidth
                val extraHeight = (scale - 1) * constraints.maxHeight

                val maxX = extraWidth / 2
                val maxY = extraHeight / 2

                offset = Offset(
                    x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                    y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY)
                )

                offset += panChange
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
                    .transformable(state)
            ) {
                items(chapterPageImages) { pageImage ->
                    val painter = rememberImagePainter(
                        data = pageImage,
                        builder = {
                            size(OriginalSize)
                        }
                    )
                    val painterState = painter.state
                    Image(
                        painter = painter,
                        contentDescription = "Page Image",
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (painterState is ImagePainter.State.Loading){
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}