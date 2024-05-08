package com.ligerinc.fruithub

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserTopComponent(){
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = R.drawable.icon_app),
            contentDescription = null,
            Modifier
                .size(40.dp,46.dp)
        )
        Text(
            text = "Account",
            fontFamily = FontFamily(
                Font(resId = R.font.jaldi_bold,
                    FontWeight.Bold)
            ),
            fontSize = 28.sp
        )
    }
}
@Preview
@Composable
fun UserInfo(userName:String = "Demo User", userEmail:String = "email@email.com",userProfile: Int = R.drawable.icon_app){
    Row (
        Modifier
            .fillMaxWidth()
            .height(65.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painterResource(id = userProfile),
            contentDescription = null,
            Modifier.size(50.dp)
        )
        Column(
            Modifier.fillMaxWidth(.8F)
        ) {
            Text(
                text = userName,
                fontFamily = FontFamily(
                    Font(resId = R.font.jaldi_bold,
                        FontWeight.Normal)
                ),
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp, 0.dp)
                    .height(25.dp),
            )
            Text(
                text = userEmail,
                fontFamily = FontFamily(
                Font(resId = R.font.jaldi_regular,
                    FontWeight.Normal)
                ),
                fontSize = 19.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp, 0.dp),
                color = Color(145,121,121),
                overflow = TextOverflow.Ellipsis
            )
        }
        Image(Icons.AutoMirrored.Filled.KeyboardArrowRight,null)
    }
}

@Preview
@Composable
fun OptionAccount(oName:String ="Option name", imageIDN: Int =R.drawable.icon_app){
    Row (
        Modifier
            .fillMaxWidth()
            .height(65.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painterResource(id = imageIDN),
            contentDescription = null,
            Modifier.size(50.dp).padding(10.dp,0.dp)
        )
        Text(
            text = oName,
            fontFamily = FontFamily(
                Font(resId = R.font.jaldi_bold,
                    FontWeight.Normal)
            ),
            fontSize = 20.sp,
            modifier = Modifier
                .padding(5.dp, 0.dp)
                .fillMaxWidth(.8F),
        )
        Image(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
    }
}

@Composable
fun LogOut(auth: FirebaseAuth,context:Context,fruitHubViewModel: FruitHubViewModel,navController: NavController){
    Row (
        Modifier
            .fillMaxWidth()
            .height(65.dp).clickable {
                auth.signOut()
                Toast.makeText(
                    context,
                    "Logged out successfully",
                    Toast.LENGTH_SHORT
                ).show()
                fruitHubViewModel.authenticated = false
                navController.navigate(MainActivityScreen.route)
            },
        horizontalArrangement = Arrangement.spacedBy(25.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = null,
            Modifier.size(50.dp).padding(10.dp,0.dp),
            tint = Color.Red
        )
        Text(
            text = "Logout",
            fontFamily = FontFamily(
                Font(resId = R.font.jaldi_bold,
                    FontWeight.Normal)
            ),
            fontSize = 20.sp,
            modifier = Modifier
                .padding(5.dp, 0.dp)
                .fillMaxWidth(),
            color = Color.Red
        )
    }
}
