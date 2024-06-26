package com.example.mangareaderui.screens.mainscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.R
import com.example.retrofit.network.model.mangadetail.MangaDetailResponse

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController: NavHostController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()



    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {

        BottomNavGraph(navController = navController, mainViewModel = mainViewModel)

    }
}

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val screens = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.Explore,
        BottomNavigationScreens.Library
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomNavigationBar = screens.any { screen ->
        screen.route == currentDestination?.route
    }

    if (showBottomNavigationBar) {
        BottomNavigation(
            backgroundColor = Color.White
        ) {
            screens.forEach {screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavigationScreens,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(
                text = screen.title,
                color = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) colorResource(id = R.color.black) else colorResource(id = R.color.light_black)
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = null,
                tint = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) colorResource(id = R.color.black) else colorResource(id = R.color.light_black)
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        selectedContentColor = Color.Black,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}


fun getMangaAuthor(mangaResponse: MangaDetailResponse): String {
    lateinit var author: String

    for (relationship in mangaResponse.data.relationships) {
        when(relationship.type) {
            "author" -> author = relationship.id
            else -> continue
        }
    }

    return author
}

fun getMangaDescription(mangaResponse: MangaDetailResponse): String {
    val description = if (!mangaResponse.data.attributes.description.en.isNullOrEmpty()) {
        mangaResponse.data.attributes.description.en
    } else if (mangaResponse.data.attributes.description.en.isNullOrEmpty() && !mangaResponse.data.attributes.description.ja.isNullOrEmpty()) {
        mangaResponse.data.attributes.description.ja
    } else {
         "<No Description!>"
    }

    return description
}

fun getMangaTags(mangaResponse: MangaDetailResponse): List<String> {
    val genre: MutableList<String> = mutableListOf()

    for (tag in mangaResponse.data.attributes.tags) {
        when(tag.attributes.group) {
            "genre" -> genre.add(tag.attributes.name.en)
            else -> continue
        }
    }

    return if (genre.isEmpty()) listOf("") else genre
}

fun getMangaCoverArtId(mangaResponse: MangaDetailResponse): String {
    lateinit var mangaCoverArtId: String

    for (relationship in mangaResponse.data.relationships) {
        when(relationship.type) {
            "cover_art" -> mangaCoverArtId = relationship.id
            else -> continue
        }
    }

    return mangaCoverArtId

}

fun getMangaName(mangaResponse: MangaDetailResponse): String {
    lateinit var mangaName: String

    if (!mangaResponse.data.attributes.title.en.isNullOrEmpty()) {
        mangaName = mangaResponse.data.attributes.title.en
    } else if (!mangaResponse.data.attributes.title.ja.isNullOrEmpty()) {
        mangaName = mangaResponse.data.attributes.title.ja
    } else {
        for (lang in mangaResponse.data.attributes.altTitles) {
            if (!lang.en.isNullOrEmpty()) {
                mangaName = lang.en
                break
            } else if (lang.en.isNullOrEmpty() && !lang.ja.isNullOrEmpty()) {
                mangaName = lang.ja
            } else if (lang.en.isNullOrEmpty() && lang.ja.isNullOrEmpty() && !lang.ko.isNullOrEmpty()) {
                mangaName = lang.ko
            }
        }
    }

    return mangaName
}

sealed class MainScreenNavGraph(
    val route: String
)  {
    object BottomGraph: MainScreenNavGraph(route = "bottom_nav_graph")
    object DetailGraph: MainScreenNavGraph(route = "detail_graph")
}