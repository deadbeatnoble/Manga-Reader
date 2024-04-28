package com.example.mangareaderui.screens.mangascreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.mangareaderui.domain.model.Page
import com.example.mangareaderui.domain.model.PageState

@Composable
fun PageView(
    page: Page
) {
    when(page.state) {
        PageState.LOADED -> {
            SubcomposeAsyncImage(
                model = page.image,
                contentDescription = "Page Image",
                loading = {
                    CircularProgressIndicator()
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        PageState.LOADING -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        PageState.FAILED -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {

                }) {
                    Text(
                        text = "Retry",
                        color = Color.Red
                    )
                }
            }
        }
    }
}