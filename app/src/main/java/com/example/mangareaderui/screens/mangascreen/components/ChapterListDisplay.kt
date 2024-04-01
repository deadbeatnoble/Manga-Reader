package com.example.mangareaderui.screens.mangascreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.domain.model.ChapterModel
import com.example.mangareaderui.screens.mainscreen.DetailNavGraph

@Composable
fun ChapterListDisplay(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    chapter: ChapterModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable {
                mainViewModel.loadSelectedChapterId(chapter.id!!)
                navController.navigate(DetailNavGraph.ChapterDisplayScreen.route)
            }
    ) {
        Text(
            text = chapter.language ?: "",
            fontSize = 16.sp,
            color = Color.LightGray
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Chapter ${chapter.chapter}")
            if (!chapter.title.isNullOrEmpty()) {
                Text(text = ": ${chapter.title}")
            }
        }
        Text(
            text = chapter.date ?: "2024-03-18",
            fontSize = 16.sp,
            color = Color.LightGray
        )
    }
}