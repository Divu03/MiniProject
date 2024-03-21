package com.littlelemon.fruithub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.littlelemon.fruithub.dao.AppDatabase
import com.littlelemon.fruithub.dao.FruitDataNetwork
import com.littlelemon.fruithub.dao.FruitNetwork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json



class MainActivity : ComponentActivity() {
// http client
    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }
// database built
    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
    }
// view model for switch
    private val fruitHubViewModel by viewModels<FruitHubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val databaseFruitData by database.fruitDataDao().getAll().observeAsState()
            val fruitDataObj by remember {  mutableStateOf(databaseFruitData) }

// navcontroller and navigation tree
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
                        CameraScreen()
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
                        UserScreen(navController)
                    }
                }
                composable("fInfo"){
                    FruitInfoScreen(fruitDataObj,navController)
                }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            if (database.fruitDataDao().isEmpty()) {
                val fruitDataNetwork = fetchMenu()
                saveFruitDataToDatabase(fruitDataNetwork)
            }
        }
    }
    private suspend fun fetchMenu(): FruitDataNetwork {
        val url ="jsonData API"
        return try {
            val jsonString = httpClient.get(url).body<String>()
            val fruitNetwork = Json.decodeFromString<FruitNetwork>(jsonString)
            fruitNetwork.fruit
        } catch (e: Exception) {
            FruitDataNetwork(0," "," "," "," "," "," "," "," ", " "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," ")
        }
    }

    private fun saveFruitDataToDatabase(fruitDataNetwork: FruitDataNetwork) {
        val fruitDataRooms = fruitDataNetwork.toFruitDataRoom()
        database.fruitDataDao().insertAll(fruitDataRooms)
    }
}

//for back staking removing if changing the graph of the navigation
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T{
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}