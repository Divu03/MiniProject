package com.littlelemon.fruithub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun ExploreScreen(fruitHubViewModel:FruitHubViewModel,navController: NavController){
    Scaffold(
        topBar = {
            TopExplore(fruitHubViewModel)
        },
        bottomBar = {
            BottomNavigation(1, navController)
        }
    ) {
            innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(fruitHubViewModel.switchState){
                ArticlesExplore()
            }else{
                FruitCard()
            }
        }
    }
}


@Composable
fun MainActivityScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigation(0,navController) },
        topBar = { TopComponent()}
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            PopularArticles()
            ExploreFruit()
            RecentFruit()
            RecentArticles()
        }
    }
}

@Composable
fun MySaveScreen(fruitHubViewModel:FruitHubViewModel,navController: NavController){
    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(5.dp)
            ){
                TopSaves()
                SavedButton(fruitHubViewModel)
            }
        }, bottomBar = {
            BottomNavigation(activityIndex = 3,navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if (fruitHubViewModel.selectedIndex == 0){
                FruitCard()
            }
            else{
                ArticlesExplore()
            }
        }
    }
}

@Composable
fun CameraScreen(){}

@Composable
fun UserScreen(navController: NavController){
    Scaffold(
        bottomBar = { BottomNavigation(4,navController) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(text = "UserScreen")
        }
    }
}