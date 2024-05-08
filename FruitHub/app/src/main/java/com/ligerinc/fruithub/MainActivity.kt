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
import androidx.compose.runtime.remember
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.ligerinc.fruithub.dao.AppDatabase
import com.ligerinc.fruithub.dao.FruitDataNetwork
import com.ligerinc.fruithub.dao.FruitList
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
//Firebase authentication
    private lateinit var auth: FirebaseAuth

// http client
    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }
//FireBase FireStore database
    private val db = Firebase.firestore



// database built
    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
    }
// view model for switch
    private val fruitHubViewModel by viewModels<FruitHubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        //Ask for Permission
        if(!hasRequiredPermissions()){
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSION,0
            )
        }


        setContent {

            //photo viewModel
            val viewModel = viewModel<ImageViewModel>()



            // controller For Camara screen in LifeCycle
            val cameraController = remember {
                LifecycleCameraController(applicationContext).apply {
                    setEnabledUseCases(
                        CameraController.IMAGE_CAPTURE
                    )
                }
            }

            // navcontroller and navigation tree
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = MainActivityScreen.route) {

                navigation(
                    "Login",
                    AuthenticationScreen.route
                ){
                    composable("Login"){
                        LoginScreen(navController, auth, applicationContext, fruitHubViewModel)
                    }
                    composable("SignUp"){
                        SignupScreen(navController, auth, applicationContext, fruitHubViewModel)
                    }
                }

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
                    composable("Camera"){backStakeEntry->
                        val viewModelNav = backStakeEntry.sharedViewModel<FruitHubViewModel>(navController)
                        CameraScreen(cameraController,applicationContext,navController,viewModel)
                    }
                    composable("imageClassification/{bitmapHashCode}") {
                        backStackEntry ->
                        val viewModelNav = backStackEntry.sharedViewModel<FruitHubViewModel>(navController)
                        val bitmapHashCode = backStackEntry.arguments?.getString("bitmapHashCode")?.toIntOrNull()

                        val bitmap = remember { viewModel.capturedBitmaps[bitmapHashCode] }
                        if (bitmap != null) {

                            ImageClassificationScreen(bitmap, applicationContext, navController, database)
                        } else {
                            navController.navigate("err")
                            Log.d("NavClass","Error screen")
                        }

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
                        UserScreen(navController,fruitHubViewModel,auth, applicationContext)
                    }
                }
                composable("fInfo/{id}/{name}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    val name = backStackEntry.arguments?.getString("name")
                    if (id != null) {

                        FruitInfoScreen(navController,database, id.toInt(), name.toString())
                    }
                }

                composable("err/{msg}"){backStackEntry ->
                    val msg = backStackEntry.arguments?.getString("msg")
                    ErrorScreen(msg.toString())
                }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            if (database.fruitDataDao().isEmptyFDR()) {
                try {
                    db.collection("Fruit_info").get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val fruitDataNetwork = document.toObject(FruitDataNetwork::class.java)
                                saveFruitDataToDatabase(fruitDataNetwork)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("FireBaseError", "Error getting documents: ", exception)
                        }
                } catch (e: Exception) {
                    Log.d("ErrorFB", e.toString())
                }
            }
            if(database.fruitDataDao().isEmptyFL()){
                database.fruitDataDao().insertAllFL(
                    FruitList(1,"Apple Braeburn", R.drawable.apple,R.drawable.apple_braeburn_info),
                    FruitList(2,"Apple Crimson Snow", R.drawable.apple_crimson_snow,R.drawable.apple_crimson_snow_info),
                    FruitList(3,"Apricot", R.drawable.apricot,R.drawable.apricot_info),
                    FruitList(4,"Avocado", R.drawable.avocado,R.drawable.avocado_info),
                    FruitList(5,"Banana", R.drawable.banana,R.drawable.banana_info),
                    FruitList(6,"Beetroot", R.drawable.beetroot,R.drawable.beetroot_info),
                    FruitList(7,"Blueberry", R.drawable.blueberry,R.drawable.blueberry_info),
                    FruitList(8,"Cactus Fruit", R.drawable.cactus_fruit,R.drawable.cactus_fruit_info),
                    FruitList(9,"Cantaloupe", R.drawable.cantaloupe,R.drawable.cantaloupe_info),
                    FruitList(10,"Carambula", R.drawable.carambula,R.drawable.carambula_info),
                    FruitList(11,"Cauliflower", R.drawable.cauliflower,R.drawable.cauliflower_info),
                    FruitList(12,"Cherry", R.drawable.cherry,R.drawable.cherry_wax_red_info),
                    FruitList(13,"Cherry rainer", R.drawable.cherry_rainer,R.drawable.cherry_rainer_info),
                    FruitList(14,"Cherry wax black", R.drawable.cherry_wax_black,R.drawable.cherry_wax_black_info),
                    FruitList(15,"Cherry wax red", R.drawable.cherry_wax_red,R.drawable.cherry_wax_red_info),
                    FruitList(16,"Cherry wax yellow", R.drawable.cherry_wax_yellow,R.drawable.cherry_wax_yellow_info),
                    FruitList(17,"Chestnut", R.drawable.chestnut,R.drawable.chestnut),
                    FruitList(18,"Clementine", R.drawable.clementine,R.drawable.clementine_info),
                    FruitList(19,"Cocos", R.drawable.cocos,R.drawable.cocos_info),
                    FruitList(20,"Corn", R.drawable.corn,R.drawable.corn_info),
                    FruitList(21,"Cucumber ripe", R.drawable.cucumber_ripe,R.drawable.cucumber_ripe_info),
                    FruitList(22,"Dates", R.drawable.dates,R.drawable.dates_info),
                    FruitList(23,"Eggplant", R.drawable.eggplant,R.drawable.eggplant_info),
                    FruitList(24,"Fig", R.drawable.fig,R.drawable.fig),
                    FruitList(25,"Ginger root", R.drawable.ginger_root,R.drawable.ginger_root_info),
                    FruitList(26,"Granadilla", R.drawable.granadilla,R.drawable.granadilla_info),
                    FruitList(27,"Grape Blue", R.drawable.grape_blue,R.drawable.grape_blue_info),
                    FruitList(28,"Grape Pink", R.drawable.grape_pink,R.drawable.grape_pink_info),
                    FruitList(29,"Grape White", R.drawable.grape_white,R.drawable.grape_white_info),
                    FruitList(30,"Guava", R.drawable.guava,R.drawable.guava_info),
                    FruitList(31,"Hazelnut", R.drawable.hazelnut,R.drawable.hazelnut_info),
                    FruitList(32,"Huckleberry", R.drawable.huckleberry,R.drawable.huckleberry_info),
                    FruitList(33,"Kaki", R.drawable.kaki,R.drawable.kaki_info),
                    FruitList(34,"Kiwi", R.drawable.kiwi,R.drawable.kiwi_info),
                    FruitList(35,"Kohlrabi", R.drawable.kohlrabi,R.drawable.kohlrabi_info),
                    FruitList(36,"Kumquats", R.drawable.kumquats,R.drawable.kumquats_info),
                    FruitList(37,"Lemon", R.drawable.lemon,R.drawable.lemon_info),
                    FruitList(38,"Lemon Meyer", R.drawable.lemon_meyer,R.drawable.lemon_meyer_info),
                    FruitList(39,"Limes", R.drawable.limes,R.drawable.limes_png),
                    FruitList(40,"Lychee", R.drawable.lychee,R.drawable.lychee_info),
                    FruitList(41,"Mandarine", R.drawable.mandarine,R.drawable.mandarine_info),
                    FruitList(42,"Mango", R.drawable.mango,R.drawable.mango_info),
                    FruitList(43,"Mango Red", R.drawable.mango_red,R.drawable.mango_red_info),
                    FruitList(44,"Mangostan", R.drawable.mangostan,R.drawable.mangostan_info),
                    FruitList(45,"Maracuja", R.drawable.maracuja,R.drawable.maracuja_info),
                    FruitList(46,"Melon Piel de Sapo", R.drawable.melon_pieel_sapo,R.drawable.melon_piel_de_sapo_info),
                    FruitList(47,"Mulberry", R.drawable.mulberry,R.drawable.mulberry_info),
                    FruitList(48,"Necteraine", R.drawable.necteraine,R.drawable.necteraine_info),
                    FruitList(49,"Nut Forest", R.drawable.nut_forest,R.drawable.nut_forest_info),
                    FruitList(50,"Nut Pecan", R.drawable.nut_pecan,R.drawable.nut_pecan_info),
                    FruitList(51,"Onion", R.drawable.onion,R.drawable.onion_info),
                    FruitList(52,"Orange", R.drawable.orange,R.drawable.orange_info),
                    FruitList(53,"Papaya", R.drawable.papaya,R.drawable.papaya_info),
                    FruitList(54,"Passion Fruit", R.drawable.passion_fruit,R.drawable.passion_fruit_info),
                    FruitList(55,"Peach", R.drawable.peach,R.drawable.peach_info),
                    FruitList(56,"Pear", R.drawable.pear,R.drawable.pear_info),
                    FruitList(57,"Pear Abate", R.drawable.pear_abate,R.drawable.pear_abate_info),
                    FruitList(58,"Pear Forelle", R.drawable.pear_forelle,R.drawable.pear_forelle_info),
                    FruitList(59,"Pear Kaiser", R.drawable.pear_kaiser,R.drawable.pear_kaise_info),
                    FruitList(60,"Pear Monster", R.drawable.pear_monster,R.drawable.pear_monster),
                    FruitList(61,"Pear Red", R.drawable.pear_red,R.drawable.pear_red_info),
                    FruitList(62,"Pear Stone", R.drawable.pear_stone,R.drawable.pear_stone),
                    FruitList(63,"Pear Williams", R.drawable.pear_williams,R.drawable.pear_williams_info),
                    FruitList(64,"Pepino", R.drawable.pepino,R.drawable.pepino_info),
                    FruitList(65,"Pepper Green", R.drawable.pepper_green,R.drawable.pepper_green_info),
                    FruitList(66,"Pepper Orange", R.drawable.pepper_orange,R.drawable.pepper_orange_info),
                    FruitList(67,"Pepper Red", R.drawable.pepper_red,R.drawable.pepper_red_info),
                    FruitList(68,"Pepper Yellow", R.drawable.pepper_yellow,R.drawable.pepper_yellow_info),
                    FruitList(69,"Physalis", R.drawable.physalis,R.drawable.physalis_info),
                    FruitList(70,"Physalis with Husk", R.drawable.physalis_with_husk,R.drawable.physalis_with_husk_info),
                    FruitList(71,"Pineapple", R.drawable.pineapple,R.drawable.pineapple_info),
                    FruitList(72,"Pitahaya Red", R.drawable.pitahaya_red,R.drawable.pitahaya_red_info),
                    FruitList(73,"Plum", R.drawable.plum,R.drawable.plum_info),
                    FruitList(74,"Pomegranate", R.drawable.pomegranate,R.drawable.pomegranate_info),
                    FruitList(75,"Pomelo Sweetie", R.drawable.pomelo_sweetie,R.drawable.pomelo_sweetie_info),
                    FruitList(76,"Potato Red", R.drawable.potato_red,R.drawable.potato_red_info),
                    FruitList(77,"Potato Sweet", R.drawable.potato_sweet,R.drawable.potato_sweet_info),
                    FruitList(78,"Potato White", R.drawable.potato_white,R.drawable.potato_white_info),
                    FruitList(79,"Quince", R.drawable.quince,R.drawable.quince_info),
                    FruitList(80,"Rambutan", R.drawable.rambutan,R.drawable.rambutan_info),
                    FruitList(81,"Raspberry", R.drawable.raspberry,R.drawable.raspberry_info),
                    FruitList(82,"Redcurrant", R.drawable.redcurrant,R.drawable.redcurrant_info),
                    FruitList(83,"Salak", R.drawable.salak,R.drawable.salak_info),
                    FruitList(84,"Strawberry", R.drawable.strawberry,R.drawable.strawberry_info),
                    FruitList(85,"Strawberry Wedge", R.drawable.strawberry_wedge,R.drawable.strawberry_wedge_info),
                    FruitList(86,"Tamarillo", R.drawable.tamarillo,R.drawable.tamarillo_info),
                    FruitList(87,"Tangelo", R.drawable.tangelo,R.drawable.tangelo_info),
                    FruitList(88,"Tomato", R.drawable.tomato,R.drawable.tomato_info),
                    FruitList(89,"Walnut", R.drawable.walnut,R.drawable.walnut_info),
                    FruitList(90,"Watermelon", R.drawable.watermelon,R.drawable.watermelon_info)
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