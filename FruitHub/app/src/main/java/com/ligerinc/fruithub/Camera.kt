package com.ligerinc.fruithub

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ligerinc.fruithub.domain.Classification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraView(
    controller: LifecycleCameraController,
    classification: List<Classification>
){
    Box(modifier = Modifier.fillMaxSize()){
        CameraPreview(
            cameraController = controller,
            Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
        )
        Column(
            Modifier.fillMaxWidth()
        ) {
            classification.forEach{
                Text(
                    text = it.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
}