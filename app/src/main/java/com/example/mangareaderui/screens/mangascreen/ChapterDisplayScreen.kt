package com.example.mangareaderui.screens.mangascreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        LazyColumn {
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