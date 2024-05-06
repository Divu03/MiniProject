package com.ligerinc.fruithub

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
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
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder

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
                    capturedBitmap = null
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                    "Click"
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
                        scope.launch{
                            capturedBitmap?.let { bitmap ->
                                classifyCapturedImage(bitmap,context)
                            }
                        }
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

@Composable
fun ImagePreview(
    bitmap: Bitmap?,
    onSend: () -> Unit,
    onRecapture: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        // Display the captured image preview if available
        bitmap?.let { bitmap ->
            Image(
                bitmap.asImageBitmap(), // Resize the bitmap to fit the screen
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Button to send the captured image
                Button(onClick = onSend) {
                    Text("Send")
                }
                // Button to recapture the image
                Button(onClick = onRecapture) {
                    Text("Recapture")
                }
            }
        }
    }
}


fun preprocessImage(bitmap: Bitmap): ByteBuffer {
    // Resize the bitmap to match the input size expected by the model (100x100)
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true)

    // Normalize pixel values to [0, 1]
    val inputBuffer = ByteBuffer.allocateDirect(100 * 100 * 3 * 4).apply {
        order(ByteOrder.nativeOrder())
        rewind()
    }
    resizedBitmap.forEachPixel { r, g, b ->
        inputBuffer.putFloat(r / 255f)
        inputBuffer.putFloat(g / 255f)
        inputBuffer.putFloat(b / 255f)
    }
    return inputBuffer
}

// Extension function to iterate through pixels of a Bitmap
inline fun Bitmap.forEachPixel(action: (r: Int, g: Int, b: Int) -> Unit) {
    val pixels = IntArray(width * height)
    getPixels(pixels, 0, width, 0, 0, width, height)
    for (i in 0 until width) {
        for (j in 0 until height) {
            val pixel = pixels[i * width + j]
            val r = Color.red(pixel)
            val g = Color.green(pixel)
            val b = Color.blue(pixel)
            action(r, g, b)
        }
    }
}

fun classifyCapturedImage(bitmap: Bitmap,context:Context) {
    try {
        // Load the TFLite model from the assets directory
        val tfliteModel = FileUtil.loadMappedFile(context, "fruithub.tflite")
        val tflite = Interpreter(tfliteModel)

        // Preprocess the captured image
        val inputBuffer = preprocessImage(bitmap)
        val outputBuffer = ByteBuffer.allocateDirect(131 * java.lang.Float.SIZE).apply {
            order(ByteOrder.nativeOrder())
            rewind()
        }
        tflite.run(inputBuffer, outputBuffer)

        val probabilities = FloatArray(131)
        outputBuffer.rewind()
        outputBuffer.asFloatBuffer().get(probabilities)

        // Find the index of the class with the highest probability
        val predictedClassIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1

        val labels = mapOf(
            0 to "Apple Braeburn",
            1 to "Apple Crimson Snow",
            2 to "Apple Golden 1",
            3 to "Apple Golden 2",
            4 to "Apple Golden 3",
            5 to "Apple Granny Smith",
            6 to "Apple Pink Lady",
            7 to "Apple Red 1",
            8 to "Apple Red 2",
            9 to "Apple Red 3",
            10 to "Apple Red Delicious",
            11 to "Apple Red Yellow 1",
            12 to "Apple Red Yellow 2",
            13 to "Apricot",
            14 to "Avocado",
            15 to "Avocado ripe",
            16 to "Banana",
            17 to "Banana Lady Finger",
            18 to "Banana Red",
            19 to "Beetroot",
            20 to "Blueberry",
            21 to "Cactus fruit",
            22 to "Cantaloupe 1",
            23 to "Cantaloupe 2",
            24 to "Carambula",
            25 to "Cauliflower",
            26 to "Cherry 1",
            27 to "Cherry 2",
            28 to "Cherry Rainier",
            29 to "Cherry Wax Black",
            30 to "Cherry Wax Red",
            31 to "Cherry Wax Yellow",
            32 to "Chestnut",
            33 to "Clementine",
            34 to "Cocos",
            35 to "Corn",
            36 to "Corn Husk",
            37 to "Cucumber Ripe",
            38 to "Cucumber Ripe 2",
            39 to "Dates",
            40 to "Eggplant",
            41 to "Fig",
            42 to "Ginger Root",
            43 to "Granadilla",
            44 to "Grape Blue",
            45 to "Grape Pink",
            46 to "Grape White",
            47 to "Grape White 2",
            48 to "Grape White 3",
            49 to "Grape White 4",
            50 to "Grapefruit Pink",
            51 to "Grapefruit White",
            52 to "Guava",
            53 to "Hazelnut",
            54 to "Huckleberry",
            55 to "Kaki",
            56 to "Kiwi",
            57 to "Kohlrabi",
            58 to "Kumquats",
            59 to "Lemon",
            60 to "Lemon Meyer",
            61 to "Limes",
            62 to "Lychee",
            63 to "Mandarine",
            64 to "Mango",
            65 to "Mango Red",
            66 to "Mangostan",
            67 to "Maracuja",
            68 to "Melon Piel de Sapo",
            69 to "Mulberry",
            70 to "Nectarine",
            71 to "Nectarine Flat",
            72 to "Nut Forest",
            73 to "Nut Pecan",
            74 to "Onion Red",
            75 to "Onion Red Peeled",
            76 to "Onion White",
            77 to "Orange",
            78 to "Papaya",
            79 to "Passion Fruit",
            80 to "Peach",
            81 to "Peach 2",
            82 to "Peach Flat",
            83 to "Pear",
            84 to "Pear 2",
            85 to "Pear Abate",
            86 to "Pear Forelle",
            87 to "Pear Kaiser",
            88 to "Pear Monster",
            89 to "Pear Red",
            90 to "Pear Stone",
            91 to "Pear Williams",
            92 to "Pepino",
            93 to "Pepper Green",
            94 to "Pepper Orange",
            95 to "Pepper Red",
            96 to "Pepper Yellow",
            97 to "Physalis",
            98 to "Physalis with Husk",
            99 to "Pineapple",
            100 to "Pineapple Mini",
            101 to "Pitahaya Red",
            102 to "Plum",
            103 to "Plum 2",
            104 to "Plum 3",
            105 to "Pomegranate",
            106 to "Pomelo Sweetie",
            107 to "Potato Red",
            108 to "Potato Red Washed",
            109 to "Potato Sweet",
            110 to "Potato White",
            111 to "Quince",
            112 to "Rambutan",
            113 to "Raspberry",
            114 to "Redcurrant",
            115 to "Salak",
            116 to "Strawberry",
            117 to "Strawberry Wedge",
            118 to "Tamarillo",
            119 to "Tangelo",
            120 to "Tomato 1",
            121 to "Tomato 2",
            122 to "Tomato 3",
            123 to "Tomato 4",
            124 to "Tomato Cherry Red",
            125 to "Tomato Heart",
            126 to "Tomato Maroon",
            127 to "Tomato not Ripened",
            128 to "Tomato Yellow",
            129 to "Walnut",
            130 to "Watermelon"
        )
        val predictedClassLabel = labels[predictedClassIndex]

        // Get the probability score of the predicted class
        val predictedProbability = probabilities[predictedClassIndex]

        // Display the predicted class label and probability to the user
        val message = "Predicted class: $predictedClassLabel\nProbability: $predictedProbability"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error classifying image", Toast.LENGTH_SHORT).show()
    }
}
