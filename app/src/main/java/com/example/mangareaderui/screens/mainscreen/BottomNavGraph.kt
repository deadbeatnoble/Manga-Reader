package com.example.mangareaderui.screens.mainscreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.R
import com.example.mangareaderui.screens.explorescreen.ExploreScreen
import com.example.mangareaderui.screens.homescreen.HomeScreen
import com.example.mangareaderui.screens.libraryscreen.LibraryScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        route = MainScreenNavGraph.BottomGraph.route,
        startDestination = BottomNavigationScreens.Home.route
    ) {
        composable(route = BottomNavigationScreens.Home.route) {
            HomeScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(route = BottomNavigationScreens.Explore.route) {
            ExploreScreen(navController = navController,mainViewModel = mainViewModel)
        }
        composable(route = BottomNavigationScreens.Library.route) {
            LibraryScreen()
        }
        detailNavGraph(navController = navController, mainViewModel = mainViewModel)
    }
}

sealed class BottomNavigationScreens(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Home: BottomNavigationScreens(route = "home_screen", title = "Home", icon = R.drawable.home_icon)
    object Explore: BottomNavigationScreens(route = "explore_screen", title = "Explore", icon = R.drawable.explore_icon)
    object Library: BottomNavigationScreens(route = "library_screen", title = "Library", icon = R.drawable.library_icon)
}