package com.example.mangareaderui.screens.mangascreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.example.mangareaderui.MainViewModel
import kotlinx.coroutines.cancel

@OptIn(ExperimentalCoilApi::class)
@SuppressLint("MutableCollectionMutableState", "CoroutineCreationDuringComposition")
@Composable
fun PageListView(
    context: Context,
    mainViewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()

    val pageImages by mainViewModel.chapterPages.collectAsState()
    Log.e("Chapter_Page_Size_1", pageImages.size.toString())


    LaunchedEffect(true){
        mainViewModel.clearChapterLinks()
        mainViewModel.fetchChapterPagesData(mainViewModel.selectedChapterId.value)

        mainViewModel.loadChapterPages(context = context)
        Log.e("Chapter_Page_Size_2", pageImages.size.toString())
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    context.imageLoader.diskCache?.clear()
                    context.imageLoader.memoryCache?.clear()
                    scope.cancel()
                }) {
                    Text(text = "Clear cache")
                }
            }
        }
        if (pageImages.isNotEmpty()) {
            items(pageImages) { img ->
                mainViewModel.onUpdate.value
                PageView(page = img)
            }
        } else {
            item{
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Fetching all pages"
                    )
                }
            }
        }
    }
}