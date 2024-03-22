package com.littlelemon.fruithub

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.littlelemon.fruithub.dao.AppDatabase
import com.littlelemon.fruithub.dao.FruitDataNetwork
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
// http client
    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    val db = Firebase.firestore

// database built
    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
    }
// view model for switch
    private val fruitHubViewModel by viewModels<FruitHubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val databaseFruitData by database.fruitDataDao().getAll().observeAsState(null)

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
                    FruitInfoScreen(databaseFruitData,navController)
                }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            if (database.fruitDataDao().isEmpty()) {
                try {
                    db.collection("Fruit_Data").document("001").get()
                        .addOnSuccessListener { result ->
                            val fruitDataNetwork = result.toObject(FruitDataNetwork::class.java)
                            if (fruitDataNetwork != null) {
                                saveFruitDataToDatabase(fruitDataNetwork)
                            }
                            Log.d("FBData", result.data.toString())
                            Log.d("NetworkData",fruitDataNetwork.toString())
                        }
                        .addOnFailureListener { exception ->
                            Log.d("FireBaseError", "Error getting documents: ", exception)
                        }
                } catch (e: Exception) {
                    // Handle exceptions
                    Log.d("FireBaseErr","Error in bringing the data")
                    Log.d("ErrorFB",e.toString())
                }
            }
        }
    }

    private fun saveFruitDataToDatabase(fruitDataNetwork: FruitDataNetwork) {
        lifecycleScope.launch(Dispatchers.IO) {
            val fruitDataRooms = fruitDataNetwork.toFruitDataRoom()
            database.fruitDataDao().insertObj(fruitDataRooms)
        }
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