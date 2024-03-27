package com.ligerinc.fruithub.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.Surface
import com.ligerinc.fruithub.domain.Classification
import com.ligerinc.fruithub.domain.FruitClassifier
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class TfLiteFruitClassifier(
    private val context: Context,
    private val threshold: Float = 0.5f,
    private val maxResults: Int = 1
): FruitClassifier {

    private var classifier: ImageClassifier? = null
    private fun setupClassifier(){
        val baseOption = BaseOptions.builder()
            .setNumThreads(2)
            .build()
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOption)
            .setMaxResults(maxResults)
            .setScoreThreshold(threshold)
            .build()
        try {
            classifier = ImageClassifier.createFromFileAndOptions(context,"fruithub.tflite",options)
        }catch (e: IllegalStateException){
            e.printStackTrace()
            Log.d("classifier",e.toString())
        }
    }
    override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
        if(classifier == null) {
            setupClassifier()
        }
        val imageProcessor = org.tensorflow.lite.support.image.ImageProcessor.Builder().build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
        val imageProcessingOptions =ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()
        val results = classifier?.classify(tensorImage,imageProcessingOptions)

        return results?.flatMap { classifications ->
            classifications.categories.map {category ->  
                Classification(
                    name = category.displayName
                )
            }
        }?.distinctBy { it.name } ?: emptyList()
    }

    private fun getOrientationFromRotation(rotation: Int):ImageProcessingOptions.Orientation{
        return when(rotation){
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }
}