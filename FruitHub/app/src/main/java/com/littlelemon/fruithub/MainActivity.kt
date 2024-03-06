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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.littlelemon.fruithub.ui.theme.FruitHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FruitHubTheme {
                Column {
                    ArticlesHome(articleTitle = "Watermelon – Juicy and Refreshing Summer Favorite")
                    FruitCard("Banana")
                    FruitCard()
                }
            }
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FruitHubTheme {
        Column(Modifier.fillMaxSize()) {
            ArticlesHome(articleTitle = "Watermelon – Juicy and Refreshing Summer Favorite")
            FruitCard("Banana")
            FruitCard()
            ArticlesExplore("Watermelon – Juicy and Refreshing Summer Favorite")
        }
    }
}