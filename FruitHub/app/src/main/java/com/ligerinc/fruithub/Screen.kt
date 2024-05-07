package com.ligerinc.fruithub

import android.content.Context
import androidx.camera.view.LifecycleCameraController
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ligerinc.fruithub.dao.AppDatabase
import com.ligerinc.fruithub.dao.FruitDataDao
import com.ligerinc.fruithub.dao.FruitDataRoom

@Composable
fun ExploreScreen(fruitHubViewModel:FruitHubViewModel,navController: NavController,fruitDataDao: FruitDataDao){
    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(5.dp)
            ){
                TopExplore(fruitHubViewModel,navController)
                SearchBarExplore(fruitDataDao)
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
                ArticlesExplore("How watermelons are healthy for you in summer season")
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
fun MySaveScreen(fruitHubViewModel:FruitHubViewModel,navController: NavController,fruitDataDao: FruitDataDao){
    var s by remember { mutableStateOf(TextFieldValue("")) }
        Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(5.dp)
            ){
                TopSaves(fruitHubViewModel,navController)
                s = searchBarSaves()
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
                FruitsSaved(navController,s)
            }
            else{
                ArticlesExplore("Why You Should Never Store Watermelon In A Fridge")
            }
        }
    }
}

@Composable
fun CameraScreen(
    cameraController:LifecycleCameraController,
    context: Context,
    navController: NavController,
    viewModel : ImageViewModel
){
    CameraView(cameraController,navController, context, viewModel)
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
            OptionAccount("App Appearance",R.drawable.icon_appearence)
            OptionAccount("Account & Security",R.drawable.icon_acc_sec)
            OptionAccount("Notification",R.drawable.icon_notification)
            OptionAccount("History",R.drawable.icon_history_user)
            OptionAccount("Help & Support",R.drawable.icon_help)
            LogOut()
        }
    }
}

@Composable
fun FruitInfoScreen(navController: NavHostController, database: AppDatabase, fruitId: Int, fruitName: String) {

    val fruitDataDao = database.fruitDataDao()

    val fruitDataRoom: FruitDataRoom? by fruitDataDao.getByIdFDR(fruitId.toString()).observeAsState()

    Column {
        TopInfo(fruitName, navController)
        FruitInfo(fruitDataRoom, navController)
    }
}


@Composable
fun ErrorScreen(msg:String){
    Text("Error Occurred   $msg")
}