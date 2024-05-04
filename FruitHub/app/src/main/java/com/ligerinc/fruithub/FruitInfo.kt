package com.ligerinc.fruithub

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.navigation.NavHostController
import com.ligerinc.fruithub.dao.FruitDataRoom

@Composable
fun FruitInfo(fDO: FruitDataRoom?, navController: NavHostController){
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {

        if (fDO != null) {
            Image(
                painter = painterResource(id = R.drawable.strawberry_info),
                contentDescription = null,
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(240.dp)
            )
            InfoText(R.drawable.icon_description,"Description",fDO.description)
            val nutritionDataTitle = listOf("Calories","Vitamins","Sugar","Protein","Carb","Fat")
            val nutritionData = listOf(fDO.calories,fDO.vitamins,fDO.sugar,fDO.protein,fDO.carbs,fDO.fat)
            val nutritionIcon = listOf(R.drawable.icon_temp,R.drawable.icon_sun,R.drawable.icon_hzone,R.drawable.icon_soil,R.drawable.icon_growthrate,R.drawable.icon_coution)
            InfoGrid(nutritionDataTitle,nutritionData,nutritionIcon,"Nutrition")
            val conditionDataTitle = listOf("Temperature","Sunlight","Hardiness Zones","Soil","Growth Rate","Cautions/Toxicity")
            val conditionData = listOf(fDO.temperature,fDO.sunLight,fDO.hardinessZone,fDO.soil,fDO.growth,fDO.cautions)
            val conditionIcon = listOf(R.drawable.icon_temp,R.drawable.icon_sun,R.drawable.icon_hzone,R.drawable.icon_soil,R.drawable.icon_growthrate,R.drawable.icon_coution)
            InfoGrid(conditionDataTitle,conditionData,conditionIcon,"Conditions")
            val careDataTitle = listOf("Water","Fertilizer","Pruning","Propagation","Repotting","Humidity")
            val careData = listOf(fDO.water,fDO.fertilizers,fDO.pruning,fDO.propagation,fDO.repotting,fDO.humidity)
            val careIcon = listOf(R.drawable.icon_waterd,R.drawable.icon_fertilizer,R.drawable.icon_pruning,R.drawable.icon_propogation,R.drawable.icon_soil,R.drawable.icon_humidity)
            InfoGrid(careDataTitle,careData,careIcon,"How to Care")
            InfoText(R.drawable.icon_description,"Common Pests & Diseases",fDO.pests)
            InfoText(R.drawable.icon_description,"Special Features",fDO.feature)
            InfoText(R.drawable.icon_description,"Uses",fDO.uses)
            InfoText(R.drawable.icon_description,"Fun Facts",fDO.funFact)
        }
        else{
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Error loading the data",
                    textAlign = TextAlign.Center
                )
                Button(onClick = { navController.navigate(MainActivityScreen.route) }) {
                    Text(
                        text = "Home",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun TopInfo(fruitName:String,navController: NavController){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 10.dp)
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
            text = fruitName,
            fontFamily = FontFamily(
                Font(resId = R.font.inter_bold,
                    FontWeight.Bold)
            ),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

