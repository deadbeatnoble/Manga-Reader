package com.example.mangareaderui.screens.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mangareaderui.MainViewModel
import com.example.mangareaderui.R
import com.example.mangareaderui.screens.explorescreen.SearchWidgetState
import com.example.mangareaderui.screens.homescreen.components.BannerDisplays
import com.example.mangareaderui.screens.homescreen.components.FinishedDisplays
import com.example.mangareaderui.screens.homescreen.components.LatestUpdatesDisplays
import com.example.mangareaderui.screens.homescreen.components.PopularDisplay
import com.example.mangareaderui.screens.homescreen.components.RecentlyAddedDisplays
import com.example.mangareaderui.screens.mainscreen.BottomNavigationScreens


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun HomeScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    /*val latestUpdatedMangas by rememberSaveable {
        mutableStateOf(mainViewModel.latestUpdatedManga.value)
    }
    val finishedMangas by rememberSaveable {
        mutableStateOf(mainViewModel.finishedManga.value)
    }
    val recentlyAddedMangas by rememberSaveable {
        mutableStateOf(mainViewModel.recentlyAddedManga.value)
    }
    val popularMangas by rememberSaveable {
        mutableStateOf(mainViewModel.popularManga.value)
    }

    val trendyMangas by rememberSaveable {
        mutableStateOf(mainViewModel.trendyManga.value!!)
    }*/

    Scaffold {
        LazyColumn(contentPadding = PaddingValues(bottom = 50.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .height(225.dp)
                        .fillMaxWidth()
                ) {
                    BannerDisplays(navController = navController, mainViewModel = mainViewModel)
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = colorResource(id = R.color.text),
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(4.dp)
                                .clickable {
                                    mainViewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                                    navController.navigate(BottomNavigationScreens.Explore.route)
                                }
                        )
                    }
                }
            }
            item {
                LatestUpdatesDisplays(mainViewModel = mainViewModel, navController = navController)
            }
            item {
                PopularDisplay(mainViewModel = mainViewModel, navController = navController)
            }
            item {
                RecentlyAddedDisplays(mainViewModel = mainViewModel, navController = navController)
            }
            item {
                FinishedDisplays(mainViewModel = mainViewModel, navController = navController)
            }

        }
    }
}

@Composable
fun HomeAppBar(
    onSearchClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "M",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ) },
        backgroundColor = Color.Transparent,
        actions = {
            IconButton(
                onClick = onSearchClicked,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.very_light_grey))
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = colorResource(id = R.color.text)
                )
            }
        },
        modifier = Modifier
            .padding(8.dp)
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController(), mainViewModel = MainViewModel())
}