package com.littlelemon.fruithub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.littlelemon.fruithub.ui.theme.FruitHubTheme

class MainActivity(private val navController: NavHostController) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNavigation()

            FruitHubTheme {
                Scaffold(
                    bottomBar = { BottomNavigation(0,navController) }
                ) { innerPadding ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        ArticlesHome(articleTitle = "Watermelon – Juicy and Refreshing Summer Favorite")
                        FruitCard("Banana")
                        FruitCard()
                        ArticlesExplore("Watermelon – Juicy and Refreshing Summer Favorite")
                    }

                }
            }
        }
    }
}


@Composable
fun MyNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MainActivityScreen.route){
        composable(MainActivityScreen.route){
            MainActivity(navController)
        }
        composable(ExploreScreen.route){
            Explore(navController)
        }
        composable(MySaveScreen.route){
            MySave(navController)
        }
    }
}

@Composable
fun ArticlesHome(articleTitle: String, articleImageId: Int = R.drawable.watermelon_article){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 220.dp, height = 197.dp)
            .padding(10.dp)
    ){
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(
                    id = articleImageId
                ),
                contentDescription ="Thumbnail for an article",
                Modifier
                    .size(210.dp, 115.dp)
                    .padding(0.dp)
            )
            Row (
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = articleTitle,
                    fontFamily = FontFamily(
                        Font(resId = R.font.jaldi_bold,
                            FontWeight.Bold)
                    ),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .size(179.dp, 44.dp)
                        .verticalScroll(ScrollState(1))
                )
                Image(
                    painter = painterResource(
                        id = R.drawable.icon_more
                    ),
                    contentDescription = null,
                    Modifier
                        .size(15.dp)
                        .padding(1.dp, 3.dp, 1.dp, 1.dp)
                        .clickable(onClick = {

                        })
                )
            }
        }
    }
}

@Composable
fun FruitCard(
    fruitName: String = "Watermelon",
    fruitImageId: Int = R.drawable.watermelon,
){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 162.dp, height = 107.dp)
            .padding(10.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = fruitName,
                fontFamily = FontFamily(
                    Font(resId = R.font.jaldi_bold,
                        FontWeight.Bold)
                ),
                textAlign = TextAlign.Left,
                fontSize = 14.sp,
                modifier = Modifier
                    .size(81.dp, 107.dp)
                    .padding(5.dp, 15.dp, 0.dp, 0.dp)
            )
            Image(
                painter = painterResource(id = fruitImageId),
                contentDescription = null,
                Modifier
                    .size(Dp(81F),Dp(107F))
            )
        }
    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = 0,
    val route: String
) 


val item = listOf(
    NavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        route = "MainActivity"
    ),
    NavigationItem(
        title = "Explore",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        hasNews = false,
        route = "Explore"
    ),
    NavigationItem(
        title = "Camara",
        selectedIcon = Icons.Filled.Create,
        unselectedIcon = Icons.Outlined.Create,
        hasNews = false,
        route = "MainActivity"
    ),
    NavigationItem(
        title = "MySaves",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        hasNews = false,
        route = "MySave"
    ),
    NavigationItem(
        title = "Account",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        hasNews = false,
        route = "MainActivity"
    )
)

@Composable
fun BottomNavigation(activityIndex:Int,navController: NavHostController){
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(activityIndex)
    }
    NavigationBar {
        item.forEachIndexed{
            index, items ->if (index!=2) {
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(items.route)
                },
                label = { Text(text = items.title) },
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) {
                            items.selectedIcon
                        } else items.unselectedIcon,
                        contentDescription = null,
                        tint = Color(red = 20, green = 140,83),
                        modifier = Modifier.size(35.dp)
                    )
                })
            }else{
                Image(
                    painter = painterResource(R.drawable.icon_camara),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                )
            }
        }
    }
}
