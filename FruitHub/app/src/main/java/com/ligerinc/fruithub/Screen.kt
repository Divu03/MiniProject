package com.ligerinc.fruithub

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.ligerinc.fruithub.dao.AppDatabase
import com.ligerinc.fruithub.dao.ArticleDao
import com.ligerinc.fruithub.dao.FruitDataDao
import com.ligerinc.fruithub.dao.FruitDataRoom
import com.ligerinc.fruithub.dao.FruitList

@Composable
fun ExploreScreen(fruitHubViewModel:FruitHubViewModel,navController: NavController,fruitDataDao: FruitDataDao,articleDao: ArticleDao){
    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(5.dp)
            ){
                TopExplore(fruitHubViewModel,navController)
                SearchBarExplore(fruitDataDao, articleDao,fruitHubViewModel.switchStateExplore)
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
fun MainActivityScreen(navController: NavController,context: Context,articleDao: ArticleDao) {
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
            PopularArticles(navController,articleDao,context)
            ExploreFruit(navController)
            RecentFruit(navController)
        }
    }
}

@Composable
fun MySaveScreen(fruitHubViewModel:FruitHubViewModel,navController: NavController,fruitDataDao: FruitDataDao,isUserLoggedIn:Boolean){
    var s by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(5.dp)
            ) {
                TopSaves(fruitHubViewModel, navController)
                s = searchBarSaves()
            }
                 }, bottomBar = {
                BottomNavigation(activityIndex = 3, navController)
            }
    ) { innerPadding ->
        if(isUserLoggedIn){
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!fruitHubViewModel.switchStateSaves) {
                    FruitsSaved(navController, s)
                } else {
                    ArticlesExplore("Why You Should Never Store Watermelon In A Fridge")
                }
            }
        }
        else{
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login to access the functionality",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigate(AuthenticationScreen.route) }) {
                        Text(text = "Login")
                    }
                }
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
fun UserScreen(navController: NavController,
               fruitHubViewModel: FruitHubViewModel,
               auth: FirebaseAuth,context: Context,
               isUserLoggedIn:Boolean,
               updateLoginState: (Boolean) -> Unit,
               sharedPreferences: SharedPreferences){

    Scaffold(
        bottomBar = { BottomNavigation(4, navController) },
        topBar = { UserTopComponent() }
    ) { innerPadding ->
        val email = sharedPreferences.getString("email", "") ?: ""
        if(isUserLoggedIn){
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                ) {
                UserInfo(email)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
                OptionAccount("App Appearance", R.drawable.icon_appearence)
                OptionAccount("Account & Security", R.drawable.icon_acc_sec)
                OptionAccount("Notification", R.drawable.icon_notification)
                OptionAccount("History", R.drawable.icon_history_user)
                OptionAccount("Help & Support", R.drawable.icon_help)
                LogOut(auth, context, fruitHubViewModel, navController){isLoggedIn ->
                    updateLoginState(isLoggedIn)
                }
            }
        }
        else{
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login to access the functionality",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigate(AuthenticationScreen.route) }) {
                        Text(text = "Login")
                    }
                }
            }
        }
    }
}

@Composable
fun FruitInfoScreen(navController: NavHostController, database: AppDatabase, fruitId: Int, fruitName: String) {

    val fruitDataDao = database.fruitDataDao()

    val fruitDataRoom: FruitDataRoom? by fruitDataDao.getByIdFDR(fruitId.toString()).observeAsState()
    val fruitDataList: FruitList? by fruitDataDao.getByNameFL(fruitName).observeAsState()

    Column {
        TopInfo(fruitName, navController)
        FruitInfo(fruitDataRoom, navController,fruitDataList)
    }
}


@Composable
fun ErrorScreen(msg:String=""){
    Text("Error Occurred   $msg")
}

@Composable
fun LoginScreen(
    navController: NavController,
    auth: FirebaseAuth,
    context: Context,
    fruitHubViewModel: FruitHubViewModel,
    updateLoginState: (Boolean) -> Unit,
    saveEmail:(String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("Home")
                            fruitHubViewModel.authenticated = true
                        } else {
                            Toast.makeText(
                                context,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                updateLoginState(true)
                saveEmail(email)
            }
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = { navController.navigate("Signup") }
        ) {
            Text("Don't have an account? Sign up")
        }
    }
}

@Composable
fun SignupScreen(
    navController: NavController,
    auth: FirebaseAuth,
    context: Context,
    fruitHubViewModel: FruitHubViewModel,
    updateLoginState: (Boolean) -> Unit,
    saveEmail:(String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(fruitHubViewModel.email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("Home")
                            fruitHubViewModel.authenticated = true
                        } else {
                            // Handle signup failure
                            Toast.makeText(
                                context,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                updateLoginState(true)
                saveEmail(email)
            }
        ) {
            Text("Sign up")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = { navController.navigate("Login") }
        ) {
            Text("Already have an account? Login")
        }
    }
}
