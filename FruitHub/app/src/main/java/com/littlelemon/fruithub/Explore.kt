package com.littlelemon.fruithub

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ArticlesExplore(
    articleTitle: String = "hi",
    articleImageId: Int = R.drawable.watermelon_article
) {
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
                        .verticalScroll(ScrollState(1)),
                    overflow = TextOverflow.Ellipsis
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
fun TopExplore(fruitHubViewModel: FruitHubViewModel){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 0.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.icon_back),
            contentDescription = null,
            alignment = Alignment.CenterStart
        )
        Text(
            text = "Explore",
            fontFamily = FontFamily(
                Font(resId = R.font.inter_bold,
                    FontWeight.Bold)
            ),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Switch(
            checked = fruitHubViewModel.switchStateExplore,
            onCheckedChange = { checked ->
                fruitHubViewModel.switchStateExplore = checked },
            thumbContent = if(!fruitHubViewModel.switchStateExplore){
                {
                    Modifier.size(24.dp)
                }
            }else{
                null
            },
            modifier = Modifier
                .padding(0.dp,0.dp,20.dp,0.dp)
        )
    }
}


val fruitlist : List<String> = listOf("Watermelon","Apple","Apple","Apple","Apple","Apple","Apple","Apple","Apple","Apple","Apple","Apple","Apple","Apple")

@Composable
fun FruitsExplore(navController: NavController) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 10.dp,
        contentPadding = PaddingValues(10.dp)
        ){
        items(fruitlist) { item->
            FruitCard(navController= navController,item)
        }
    }
}