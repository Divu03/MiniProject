package com.littlelemon.fruithub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.littlelemon.fruithub.ui.theme.FruitHubTheme

class Explore(private val navController: NavHostController) : ComponentActivity() {

    private val switchViewModel by viewModels<SwitchViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FruitHubTheme {
                Scaffold(
                    topBar = {
                        TopExplore(switchViewModel)
                    },
                    bottomBar = {
                        BottomNavigation(1,navController)
                    }
                ) {
                        innerPadding ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        if(switchViewModel.switchState){
                            ArticlesExplore()
                        }else{
                            FruitCard()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ArticlesExplore(articleTitle: String = "hi", articleImageId: Int = R.drawable.watermelon_article){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .height(297.dp)
            .padding(25.dp, 10.dp),
    ){
        Column(
            Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(
                    id = articleImageId
                ),
                contentDescription ="Thumbnail for an article",
                Modifier
                    .size(343.dp, 201.dp)
                    .padding(0.dp)
            )
            Row (
                Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = articleTitle,
                    fontFamily = FontFamily(
                        Font(resId = R.font.jaldi_bold,
                            FontWeight.Bold)
                    ),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxSize(.95F)
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
fun TopExplore(switchViewModel: SwitchViewModel){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 0.dp)
    ){
        Image(painter = painterResource(id = R.drawable.icon_back), contentDescription = null)
        Text(
            text = "Explore",
            fontFamily = FontFamily(
                Font(resId = R.font.inter_bold,
                    FontWeight.Bold)
            ),
            fontSize = 24.sp,
        )
        Switch(
            checked = switchViewModel.switchState,
            onCheckedChange = { checked ->
                switchViewModel.switchState = checked },
            thumbContent = if(!switchViewModel.switchState){
                {
                    Modifier.size(24.dp)
                }
            }else{
                null
            }
        )
    }
}



