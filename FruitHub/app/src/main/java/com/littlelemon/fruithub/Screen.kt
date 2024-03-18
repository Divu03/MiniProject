package com.littlelemon.fruithub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ExploreScreen(fruitHubViewModel:FruitHubViewModel,navController: NavController){
    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(5.dp)
            ){
                TopExplore(fruitHubViewModel)
                SearchBar()
            }
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
            if(fruitHubViewModel.switchStateExplore){
                ArticlesExplore()
            }else{
                FruitsExplore()
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
                TopSaves(fruitHubViewModel)
                SearchBar()
            }
        }, bottomBar = {
            BottomNavigation(activityIndex = 3,navController)
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!fruitHubViewModel.switchStateSaves){
                FruitsSaved()
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
        bottomBar = { BottomNavigation(4,navController) },
        topBar = { UserTopComponent()}
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            UserInfo()
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp))
            OptionAccount("Logout")
            OptionAccount()
            OptionAccount()
            OptionAccount()
            OptionAccount()
            OptionAccount()

        }
    }
}