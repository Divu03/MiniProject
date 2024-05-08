package com.ligerinc.fruithub

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.ligerinc.fruithub.dao.Article
import com.ligerinc.fruithub.dao.ArticleDao
import com.ligerinc.fruithub.dao.FruitDataDao
import com.ligerinc.fruithub.dao.FruitList

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
fun SearchBarExplore(
    fruitDataDao: FruitDataDao,
    articleDao: ArticleDao,
    isArticleView: Boolean
) {
    var searchPhrase by remember { mutableStateOf(TextFieldValue("")) }
    var suggestions by remember { mutableStateOf<List<Any>>(emptyList()) } // Use Any to handle both FruitList and Article

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(searchPhrase.text) {
        if (searchPhrase.text.isNotBlank()) {
            if (isArticleView) {
                val observer = Observer<List<Article>> { articles ->
                    suggestions = articles
                }
                // Assuming you have a function to search articles by name
                // Replace this with your actual DAO function
                articleDao.searchArticlesByName(searchPhrase.text)
                    .observe(lifecycleOwner, observer)
            } else {
                val observer = Observer<List<FruitList>> { fruits ->
                    suggestions = fruits
                }
                fruitDataDao.searchByNameFL(searchPhrase.text)
                    .observe(lifecycleOwner, observer)
            }
        } else {
            suggestions = emptyList()
        }
    }

    Column {
        OutlinedTextField(
            value = searchPhrase,
            onValueChange = { searchPhrase = it },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 5.dp),
            shape = RoundedCornerShape(20.dp)
        )
        suggestions.forEach { suggestion ->
            if (isArticleView) {
                Text(
                    text = (suggestion as Article).title,
                    modifier = Modifier
                        .clickable {
                            // Handle click on article
                        }
                        .padding(8.dp)
                )
            } else {
                Text(
                    text = (suggestion as FruitList).name,
                    modifier = Modifier
                        .clickable {
                            // Handle click on fruit
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun TopExplore(fruitHubViewModel: FruitHubViewModel,navController: NavController){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 0.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.icon_back),
            contentDescription = null,
            alignment = Alignment.CenterStart,
            modifier = Modifier.clickable {
                navController.popBackStack()
            }
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


val fruitlist : List<FruitList> = listOf(
    FruitList(1,"Apple Braeburn",R.drawable.apple,R.drawable.apple_braeburn_info),
    FruitList(5,"Banana",R.drawable.banana,R.drawable.banana_info),
    FruitList(90,"Watermelon",R.drawable.watermelon,R.drawable.watermelon),
    FruitList(12,"Cherry",R.drawable.cherry,R.drawable.cherry_wax_red_info),
    FruitList(10,"Carambula",R.drawable.carambula,R.drawable.carambula_info)
)

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