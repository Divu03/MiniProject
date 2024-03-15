package com.littlelemon.fruithub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.littlelemon.fruithub.ui.theme.FruitHubTheme

class MainActivity : ComponentActivity() {
    private val fruitHubViewModel by viewModels<FruitHubViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FruitHubTheme {
            }
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = MainActivityScreen.route) {
                navigation("Home",
                MainActivityScreen.route
                ){
                    composable("Home") {
                        MainActivityScreen(navController)
                    }
                }
                navigation(
                    "Explore",
                    ExploreScreen.route
                ){
                    composable("Explore"){
                        ExploreScreen(fruitHubViewModel,navController)
                    }
                }
                navigation(
                    "Camera",
                    CameraScreen.route
                ){
                    composable("Camera"){

                    }
                }
                navigation(
                    "MySave",
                    MySaveScreen.route
                ){
                    composable("MySave"){
                        MySaveScreen(fruitHubViewModel, navController)
                    }
                }
                navigation(
                    "User",
                    UserScreen.route
                ){
                    composable("User"){

                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T{
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}