package com.example.mangareaderui.screens.mangascreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mangareaderui.R

@Composable
fun GenreListDisplay(
    genres: List<String>,
    modifier: Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(genres) { genre ->
            Text(
                text = genre,
                color = Color.Black,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.light_grey))
                    .padding(6.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}