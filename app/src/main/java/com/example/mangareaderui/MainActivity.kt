package com.example.mangareaderui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mangareaderui.screens.mainscreen.MainScreen
import com.example.mangareaderui.ui.theme.MangaReaderUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MangaReaderUITheme {
                MainScreen()
            }
        }
    }
}