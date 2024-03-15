package com.littlelemon.fruithub

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopSaves(){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 0.dp)
    ){
        Image(painter = painterResource(id = R.drawable.icon_back), contentDescription = null)
        Text(
            text = "My Saves",
            fontFamily = FontFamily(
                Font(resId = R.font.inter_bold,
                    FontWeight.Bold)
            ),
            fontSize = 24.sp,
        )
        Image(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            Modifier.clickable() {

            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedButton(fruitHubViewModel: FruitHubViewModel){
    val options = mutableListOf("Fruits","Articles")

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed{ index, option ->
            SegmentedButton(
                selected = fruitHubViewModel.selectedIndex == index,
                onClick = { fruitHubViewModel.selectedIndex = index },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                Modifier.padding(0.dp,10.dp),

            ) {
                Text(text = option)
            }
        }
    }
}

