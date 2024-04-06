package com.example.mangareaderui.screens.mangascreen

import android.util.Log
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mangareaderui.MainViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChapterDisplayScreen(
    mainViewModel: MainViewModel
) {
    val chapterPageImages = mainViewModel.chapterLinks.value

    LaunchedEffect(true){
        mainViewModel.clearChapterLinks()
        mainViewModel.fetchChapterPagesData(mainViewModel.selectedChapterId.value)
        Log.e("CHAPTER_PAGE_IMAGES", mainViewModel.chapterLinks.value.toString())
    }

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
                    GlideImage(
                        model = pageImage,
                        contentDescription = "Page Image",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}