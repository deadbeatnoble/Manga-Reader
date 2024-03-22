package com.example.mangareaderui.screens.mangascreen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DescriptionDisplay(
    description: String,
    modifier: Modifier
) {
    var showMore by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(animationSpec = tween(100))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { showMore = !showMore }
        ) {
            if (showMore) {
                Text(
                    text = description,
                    color = Color.Black,
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            } else {
                Text(
                    text = description,
                    color = Color.Black,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawWithCache {
                            val gradient = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.8f)
                                ),
                                startY = size.height / 3,
                                endY = size.height
                            )
                            onDrawWithContent {
                                drawContent()
                                drawRect(gradient, blendMode = BlendMode.Multiply)
                            }
                        }
                        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }
        }
    }
}