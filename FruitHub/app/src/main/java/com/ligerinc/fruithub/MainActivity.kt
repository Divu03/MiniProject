package com.ligerinc.fruithub

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.ligerinc.fruithub.dao.AppDatabase
import com.ligerinc.fruithub.dao.FruitDataNetwork
import com.ligerinc.fruithub.data.TfLiteFruitClassifier
import com.ligerinc.fruithub.domain.Classification
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

    private val db = Firebase.firestore

// database built
    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
    }
// view model for switch
    private val fruitHubViewModel by viewModels<FruitHubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Ask for Permission
        if(!hasRequiredPermissions()){
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSION,0
            )
        }

        setContent {

            //photo viewModel
            val viewModel = viewModel<ImageViewModel>()

            var classifications by remember {
                mutableStateOf(emptyList<Classification>())
            }

            val analyzer = remember {
                FruitImageAnalyzer(
                    classifier = TfLiteFruitClassifier(
                        context = applicationContext
                    ),
                    onResult ={
                        classifications = it
                    }
                )
            }

            // controller For Camara screen in LifeCycle
            val cameraController = remember {
                LifecycleCameraController(applicationContext).apply {
                    setEnabledUseCases(
                        CameraController.IMAGE_ANALYSIS
                    )
                    setImageAnalysisAnalyzer(
                        ContextCompat.getMainExecutor(applicationContext),
                        analyzer
                    )
                }
            }

            val databaseFruitData by database.fruitDataDao().getAllFDR().observeAsState(null)

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
                    CameraScreenDestination.route
                ){
                    composable("Camera"){
                        CameraScreen(cameraController,applicationContext,classifications)
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
            if (database.fruitDataDao().isEmptyFDR()) {
                try {
                    db.collection("Fruit_info").document("1").get()
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
                    Log.d("ErrorFB",e.toString())
                }
            }
        }
    }

    private fun saveFruitDataToDatabase(fruitDataNetwork: FruitDataNetwork) {
        lifecycleScope.launch(Dispatchers.IO) {
            val fruitDataRooms = fruitDataNetwork.toFruitDataRoom()
            database.fruitDataDao().insertObjFDR(fruitDataRooms)
        }
    }

    //Check if we have Permission
    private fun hasRequiredPermissions(): Boolean{
        return CAMERAX_PERMISSION.all{
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    //Permission Record
    companion object{
        private val CAMERAX_PERMISSION = arrayOf(
            Manifest.permission.CAMERA
        )
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

//takePhoto Helper Function

fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    context: Context
){
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                    postScale(1f,1f)
                }

                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                Toast.makeText(context,"Captured",Toast.LENGTH_SHORT).show()

                onPhotoTaken(rotatedBitmap)
            }
            override fun onError(exception: ImageCaptureException){
                super.onError(exception)
                Log.e("Camera","Couldn't take photo ",exception)
            }
        }
    )
}