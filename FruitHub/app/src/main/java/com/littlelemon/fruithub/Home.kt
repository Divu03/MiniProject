package com.littlelemon.fruithub

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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

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
                    .size(Dp(81F), Dp(107F))
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
    val route : String
)


val item = listOf(
    NavigationItem(
        title = "MainActivity",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        route = MainActivityScreen.route
    ),
    NavigationItem(
        title = "Explore",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        hasNews = false,
        route = ExploreScreen.route
    ),
    NavigationItem(
        title = "Camera",
        selectedIcon = Icons.Filled.Create,
        unselectedIcon = Icons.Outlined.Create,
        hasNews = false,
        route = CameraScreen.route
    ),
    NavigationItem(
        title = "MySaves",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        hasNews = false,
        route = MySaveScreen.route
    ),
    NavigationItem(
        title = "User",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        hasNews = false,
        route = UserScreen.route
    )
)

@Composable
fun BottomNavigation(activityIndex: Int, navController: NavController){
    //var selectedItemIndex by rememberSaveable {        mutableIntStateOf(activityIndex)    }
    var selectedItemIndex = activityIndex
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Update selected item index based on the current route
    selectedItemIndex = when (currentRoute) {
        MainActivityScreen.route -> 0
        ExploreScreen.route -> 1
        CameraScreen.route -> 2
        MySaveScreen.route -> 3
        UserScreen.route -> 4
        else -> selectedItemIndex
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
                        tint = Color(red = 20, green = 140,83)
                    )
                })
            }else{
                Image(
                    painter = painterResource(R.drawable.icon_camara),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                )
            }
        }
    }
}