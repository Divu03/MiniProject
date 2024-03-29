package com.ligerinc.fruithub

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ligerinc.fruithub.dao.FruitList


@Composable
fun TopSaves(fruitHubViewModel: FruitHubViewModel){
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
            text = "My Saves",
            fontFamily = FontFamily(
                Font(resId = R.font.inter_bold,
                    FontWeight.Bold)
            ),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Switch(
            checked = fruitHubViewModel.switchStateSaves,
            onCheckedChange = { checked ->
                fruitHubViewModel.switchStateSaves = checked },
            thumbContent = if(!fruitHubViewModel.switchStateSaves){
                {
                    Modifier.size(24.dp)
                }
            }else{
                null
            }
        )
    }
}
val savedfruits : List<FruitList> = listOf(
    FruitList(1,"Watermelon",R.drawable.watermelon),
    FruitList(1,"Apple",R.drawable.apple),
    FruitList(1,"Banana",R.drawable.banana),
    FruitList(1,"Cherry",R.drawable.cherry),
    FruitList(1,"Carambula",R.drawable.carambula)
)

@Composable
fun FruitsSaved(navController:NavController) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 10.dp,
        contentPadding = PaddingValues(10.dp)
    ){
        items(savedfruits) { item->
            FruitCard(navController,item.name,item.imageId)
        }
    }
}

