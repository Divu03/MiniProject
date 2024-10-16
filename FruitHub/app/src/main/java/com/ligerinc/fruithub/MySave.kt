package com.ligerinc.fruithub

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ligerinc.fruithub.dao.FruitList


//Top Screen for Saved Item
@Composable
fun TopSaves(fruitHubViewModel: FruitHubViewModel,navController: NavController){
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
    FruitList(90,"Watermelon",R.drawable.watermelon,R.drawable.carambula_info),
    FruitList(1,"Apple Braeburn",R.drawable.apple,R.drawable.carambula_info),
    FruitList(5,"Banana",R.drawable.banana,R.drawable.carambula_info),
    FruitList(12,"Cherry",R.drawable.cherry,R.drawable.carambula_info),
    FruitList(10,"Carambula",R.drawable.carambula,R.drawable.carambula_info)
)

// This shows fruits that are saved : Not Needed
@Composable
fun FruitsSaved(navController:NavController,searchPhrase:TextFieldValue) {
    var suggestions by remember { mutableStateOf<List<FruitList>>(emptyList()) }

    LaunchedEffect(searchPhrase.text) {
        suggestions = if (searchPhrase.text.isNotBlank()) {
            savedfruits.filter { fruit ->
                fruit.name.contains(searchPhrase.text, ignoreCase = true)
            }
        } else {
            emptyList()
        }
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 10.dp,
        contentPadding = PaddingValues(10.dp)
    ){
        if(suggestions.isEmpty()){
            items(savedfruits) { item->
                FruitCard(navController,item)
            }
        }
        else{
            items(suggestions) { item->
                FruitCard(navController,item)
            }
        }
    }
}

//Search Bar for Saved Fruits : We do not need it write now
@Composable
fun searchBarSaves():TextFieldValue {
    var searchPhrase by remember { mutableStateOf(TextFieldValue("")) }

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
    }
    return searchPhrase
}


