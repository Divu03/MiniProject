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
import com.ligerinc.fruithub.dao.FruitList
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
                        ExploreScreen(fruitHubViewModel,navController,database.fruitDataDao())
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
                        MySaveScreen(fruitHubViewModel, navController,database.fruitDataDao())

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
            if(database.fruitDataDao().isEmptyFL()){
                database.fruitDataDao().insertAllFL(
                    FruitList(1,"Apple Braeburn", R.drawable.apple),
                    FruitList(2,"Apple Crimson Snow", R.drawable.apple_crimson_snow),
                    FruitList(3,"Apricot", R.drawable.apricot),
                    FruitList(4,"Avocado", R.drawable.avocado),
                    FruitList(5,"Banana", R.drawable.banana),
                    FruitList(6,"Beetroot", R.drawable.beetroot),
                    FruitList(7,"Blueberry", R.drawable.blueberry),
                    FruitList(8,"Cactus Fruit", R.drawable.cactus_fruit),
                    FruitList(9,"Cantaloupe", R.drawable.cantaloupe),
                    FruitList(10,"Carambula", R.drawable.carambula),
                    FruitList(11,"Cauliflower", R.drawable.cauliflower),
                    FruitList(12,"Cherry", R.drawable.cherry),
                    FruitList(13,"Cherry rainer", R.drawable.cherry_rainer),
                    FruitList(14,"Cherry wax black", R.drawable.cherry_wax_black),
                    FruitList(15,"Cherry wax red", R.drawable.cherry_wax_red),
                    FruitList(16,"Cherry wax yellow", R.drawable.cherry_wax_yellow),
                    FruitList(17,"Chestnut", R.drawable.chestnut),
                    FruitList(18,"Clementine", R.drawable.clementine),
                    FruitList(19,"Cocos", R.drawable.cocos),
                    FruitList(20,"Corn", R.drawable.corn),
                    FruitList(21,"Cucumber ripe", R.drawable.cucumber_ripe),
                    FruitList(22,"Dates", R.drawable.dates),
                    FruitList(23,"Eggplant", R.drawable.eggplant),
                    FruitList(24,"Fig", R.drawable.fig),
                    FruitList(25,"Ginger root", R.drawable.ginger_root),
                    FruitList(26,"Granadilla", R.drawable.granadilla),
                    FruitList(27,"Grape blue", R.drawable.grape_blue),
                    FruitList(28,"Grape pink", R.drawable.grape_pink),
                    FruitList(29,"GrapeWhite", R.drawable.grape_white),
                    FruitList(30,"Guava", R.drawable.guava),
                    FruitList(31,"Hazelnut", R.drawable.hazelnut),
                    FruitList(32,"Huckleberry", R.drawable.huckleberry),
                    FruitList(33,"Kaki", R.drawable.kaki),
                    FruitList(34,"Kiwi", R.drawable.kiwi),
                    FruitList(35,"Kohlrabi", R.drawable.kohlrabi),
                    FruitList(36,"Kumquats", R.drawable.kumquats),
                    FruitList(37,"Lemon", R.drawable.lemon),
                    FruitList(38,"Lemon Meyer", R.drawable.lemon_meyer),
                    FruitList(39,"Limes", R.drawable.limes),
                    FruitList(40,"Lychee", R.drawable.lychee),
                    FruitList(41,"Mandarine", R.drawable.mandarine),
                    FruitList(42,"Mango", R.drawable.mango),
                    FruitList(43,"Mango Red", R.drawable.mango_red),
                    FruitList(44,"Mangostan", R.drawable.mangostan),
                    FruitList(45,"Maracuja", R.drawable.maracuja),
                    FruitList(46,"Melon Pieel Sapo", R.drawable.melon_pieel_sapo),
                    FruitList(47,"Mulberry", R.drawable.mulberry),
                    FruitList(48,"Necteraine", R.drawable.necteraine),
                    FruitList(49,"Nut Forest", R.drawable.nut_forest),
                    FruitList(50,"Nut Pecan", R.drawable.nut_pecan),
                    FruitList(51,"Onion", R.drawable.onion),
                    FruitList(52,"Orange", R.drawable.orange),
                    FruitList(53,"Papaya", R.drawable.papaya),
                    FruitList(54,"Passion Fruit", R.drawable.passion_fruit),
                    FruitList(55,"Peach", R.drawable.peach),
                    FruitList(56,"Pear", R.drawable.pear),
                    FruitList(57,"Pear Abate", R.drawable.pear_abate),
                    FruitList(58,"Pear Forelle", R.drawable.pear_forelle),
                    FruitList(59,"Pear Kaiser", R.drawable.pear_kaiser),
                    FruitList(60,"Pear Monster", R.drawable.pear_monster),
                    FruitList(61,"Pear Red", R.drawable.pear_red),
                    FruitList(62,"Pear Stone", R.drawable.pear_stone),
                    FruitList(63,"Pear Williams", R.drawable.pear_williams),
                    FruitList(64,"Pepino", R.drawable.pepino),
                    FruitList(65,"Pepper Green", R.drawable.pepper_green),
                    FruitList(66,"Pepper Orange", R.drawable.pepper_orange),
                    FruitList(67,"Pepper Red", R.drawable.pepper_red),
                    FruitList(68,"Pepper Yellow", R.drawable.pepper_yellow),
                    FruitList(69,"Physalis", R.drawable.physalis),
                    FruitList(70,"Physalis with Husk", R.drawable.physalis_with_husk),
                    FruitList(71,"Pineapple", R.drawable.pineapple),
                    FruitList(72,"Pitahaya Red", R.drawable.pitahaya_red),
                    FruitList(73,"Plum", R.drawable.plum),
                    FruitList(74,"Pomegranate", R.drawable.pomegranate),
                    FruitList(75,"Pomelo Sweetie", R.drawable.pomelo_sweetie),
                    FruitList(76,"Potato Red", R.drawable.potato_red),
                    FruitList(77,"Potato Sweet", R.drawable.potato_sweet),
                    FruitList(78,"Potato White", R.drawable.potato_white),
                    FruitList(79,"Quince", R.drawable.quince),
                    FruitList(80,"Rambutan", R.drawable.rambutan),
                    FruitList(81,"Raspberry", R.drawable.raspberry),
                    FruitList(82,"Redcurrant", R.drawable.redcurrant),
                    FruitList(83,"Salak", R.drawable.salak),
                    FruitList(84,"Strawberry", R.drawable.strawberry),
                    FruitList(85,"Strawberry Wedge", R.drawable.strawberry_wedge),
                    FruitList(86,"Tamarillo", R.drawable.tamarillo),
                    FruitList(87,"Tangelo", R.drawable.tangelo),
                    FruitList(88,"Tomato", R.drawable.tomato),
                    FruitList(89,"Walnut", R.drawable.walnut),
                    FruitList(90,"Watermelon", R.drawable.watermelon)
                )
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