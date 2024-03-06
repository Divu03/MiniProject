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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.littlelemon.fruithub.ui.theme.FruitHubTheme

class Explore : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FruitHubTheme {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    ArticlesExplore()
                }
            }
        }
    }
}
@Composable
fun ArticlesExplore(articleTitle: String = "hi", articleImageId: Int = R.drawable.watermelon_article){
    var checked by remember { mutableStateOf(true) }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 346.dp, height = 297.dp)
            .padding(10.dp),
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
    Switch(
        checked = checked,
        onCheckedChange = { checked = it },
        thumbContent = if(!checked){
            {
                Modifier.size(24.dp)
            }
        }else{
            null
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FruitHubTheme {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
        ArticlesExplore()
        }
    }
}