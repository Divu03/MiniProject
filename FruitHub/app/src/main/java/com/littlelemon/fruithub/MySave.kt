package com.littlelemon.fruithub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavHostController
import com.littlelemon.fruithub.ui.theme.FruitHubTheme

class MySave(private val navController: NavHostController) : ComponentActivity() {
    private val switchViewModel by viewModels<SwitchViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FruitHubTheme {
                Scaffold(
                    topBar = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(5.dp)
                        ){
                            TopSaves()
                            SavedButton(switchViewModel)
                        }
                    }, bottomBar = {
                        BottomNavigation(activityIndex = 3, navController = navController)
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        if (switchViewModel.selectedIndex == 0){
                            FruitCard()
                        }
                        else{
                            ArticlesExplore()
                        }

                    }
                }
            }
        }
    }
}

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
fun SavedButton(switchViewModel: SwitchViewModel){
    val options = mutableListOf("Fruits","Articles")

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed{ index, option ->
            SegmentedButton(
                selected = switchViewModel.selectedIndex == index,
                onClick = { switchViewModel.selectedIndex = index },
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

