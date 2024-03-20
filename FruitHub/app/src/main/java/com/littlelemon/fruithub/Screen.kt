package com.littlelemon.fruithub

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            LogOut()

        }
    }
}

@Preview
@Composable
fun FruitInfoScreen(){
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.strawberry_info),
            contentDescription = null,
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(240.dp)
        )
        Column {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Image(
                    painterResource(R.drawable.photo_gallary),
                    contentDescription = null,
                    Modifier
                        .size(44.dp)
                        .padding(10.dp, 0.dp)
                )
                Text(
                    text = "Photo Gallery",
                    fontFamily = FontFamily(
                        Font(
                            resId = R.font.jaldi_bold,
                            FontWeight.Normal
                        )
                    ),
                    fontSize = 22.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 0.dp),
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                Modifier.horizontalScroll(rememberScrollState())
            ){
                Image(
                    painter = painterResource(id = R.drawable.strawberry_info),
                    contentDescription = null,
                    Modifier
                        .height(155.dp)
                        .padding(10.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.strawberry_info),
                    contentDescription = null,
                    Modifier
                        .height(155.dp)
                        .padding(10.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.strawberry_info),
                    contentDescription = null,
                    Modifier
                        .height(155.dp)
                        .padding(10.dp)
                )
            }
            InfoText(R.drawable.icon_description,"Common Pests & Diseases")
            InfoText(R.drawable.icon_description,"Special Features")
            InfoText(R.drawable.icon_description,"Uses")
            InfoText(R.drawable.icon_description,"Fun Facts")
        }
    }
}