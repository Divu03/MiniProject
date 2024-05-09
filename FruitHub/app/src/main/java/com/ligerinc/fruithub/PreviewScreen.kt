package com.ligerinc.fruithub

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ligerinc.fruithub.dao.AppDatabase

@Composable
fun PreviewScreen(bitmap: Bitmap, onRetry: () -> Unit, onSend: () -> Unit) {
    Scaffold(
        content = {padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap.asImageBitmap(), // Resize the bitmap to fit the screen
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                    Button(onClick = onSend) {
                        Text("Send")
                    }
                }
            }
        }
    )
}

@Composable
fun ImageClassificationScreen(bitmap: Bitmap, context: Context, navController: NavController, fruitData: AppDatabase) {
    val result = remember { mutableStateOf<String?>(null) }



    LaunchedEffect(true) {
        result.value = classifyCapturedImage(bitmap, context)
    }
    result.value?.let { classifiedResult ->
        // Observe the LiveData returned by the database query
        fruitData.fruitDataDao().getByNameFL(classifiedResult).observeAsState().value?.let { fruit ->
            if (fruit != null) {
                navController.navigate("fInfo/${fruit.name}"){
                    popUpTo(CameraScreenDestination.route)
                }
            } else {
                navController.navigate("err/image classification"){
                    popUpTo(CameraScreenDestination.route)
                }
            }
        }
    }

}

