package com.ligerinc.fruithub

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraView(
    controller: LifecycleCameraController,
    navController: NavController,
    context: Context
){

    val scope = rememberCoroutineScope()
    val viewModel = remember { ImageViewModel() } // Create the ImageViewModel internally
    val bitmaps by viewModel.bitmap.collectAsState()

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showPreview by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ){
        CameraPreview(
            cameraController = controller,
            modifier = Modifier.fillMaxSize()
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround
        ){

            IconButton(onClick = {
                takePhoto(controller,viewModel::onTakePhoto,context)
                // Show the captured image preview
                showPreview = true
                // Set a timeout to hide the preview after 5 seconds
                scope.launch {
                    delay(5000)
                    showPreview = false
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                    "Open Gallery"
                )
            }
        }

        // Display the captured image preview if available and showPreview is true
        if (showPreview) {
            capturedBitmap?.let { bitmap ->
                Image(
                    bitmap.asImageBitmap(), // Resize the bitmap to 100x100 pixels
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    // Button to send the captured image
                    Button(onClick = {
                        // Call a function to send the image
                        // sendImage(bitmap)
                        Toast.makeText(context, "Image sent", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Send")
                    }
                    // Button to recapture the image
                    Button(onClick = {
                        // Navigate back to the camera screen
                        showPreview = false
                    }) {
                        Text("Recapture")
                    }
                }
            }
        }

        // Update capturedBitmap when a new bitmap is captured
        if (bitmaps.isNotEmpty()) {
            capturedBitmap = bitmaps.last() // Update with the latest captured bitmap
        }
    }
}
