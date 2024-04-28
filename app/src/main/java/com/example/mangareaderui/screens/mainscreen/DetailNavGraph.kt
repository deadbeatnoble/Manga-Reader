package com.example.mangareaderui.screens.mainscreen

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.screens.mangascreen.MangaScreen
import com.example.mangareaderui.screens.mangascreen.PageListView

fun NavGraphBuilder.detailNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    navigation(
        route = MainScreenNavGraph.DetailGraph.route,
        startDestination = DetailNavGraph.MangaDetailScreen.route
    ) {
        composable(route = DetailNavGraph.MangaDetailScreen.route) {
            MangaScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(route = DetailNavGraph.ChapterDisplayScreen.route) {
            PageListView(context = LocalContext.current, mainViewModel = mainViewModel)
        }
    }
}

sealed class DetailNavGraph(
    val route: String
) {
    object MangaDetailScreen: DetailNavGraph(route = "manga_detail_screen")
    object ChapterDisplayScreen: DetailNavGraph(route = "chapter_display_screen")
}