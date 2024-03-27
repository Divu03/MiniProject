package com.ligerinc.fruithub

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ligerinc.fruithub.dao.FruitDataRoom

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
                FruitsExplore(navController)
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
            ExploreFruit(navController)
            RecentFruit(navController)
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
                FruitsSaved(navController)
            }
            else{
                ArticlesExplore()
            }
        }
    }
}

@Composable
fun CameraScreen(
    cameraController:LifecycleCameraController,
    context: Context
){
    CameraView(cameraController,context)
}

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

@Composable
fun FruitInfoScreen(fDO: FruitDataRoom?, navController: NavHostController){
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
        if (fDO != null) {
            InfoText(R.drawable.icon_description,"Description",fDO.description)
            val nutritionDataTitle = listOf("Calories","Vitamins","Sugar","Protein","Carb","Fat")
            val nutritionData = listOf(fDO.calories,fDO.vitamins,fDO.sugar,fDO.protein,fDO.carbs,fDO.fat)
            InfoGrid(nutritionDataTitle,nutritionData)
            val conditionDataTitle = listOf("Temperature","Sunlight","Hardiness Zones","Soil","Growth Rate","Cautions/Toxicity")
            val conditionData = listOf(fDO.temperature,fDO.sunLight,fDO.hardinessZone,fDO.soil,fDO.growth,fDO.cautions)
            InfoGrid(conditionDataTitle,conditionData)
            val careDataTitle = listOf("Water","Fertilizer","Pruning","Propagation","Repotting","Humidity")
            val careData = listOf(fDO.water,fDO.fertilizer,fDO.pruning,fDO.propagation,fDO.repotting,fDO.humidity)
            InfoGrid(careDataTitle,careData)
            InfoText(R.drawable.icon_description,"Common Pests & Diseases",fDO.pests)
            InfoText(R.drawable.icon_description,"Special Features",fDO.features)
            InfoText(R.drawable.icon_description,"Uses",fDO.uses)
            InfoText(R.drawable.icon_description,"Fun Facts",fDO.funFacts)
        }
        else{
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Error loading the data",
                    textAlign = TextAlign.Center
                )
                Button(onClick = { navController.navigate(MainActivityScreen.route) }) {
                    Text(
                        text = "Home",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}