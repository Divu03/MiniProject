package com.ligerinc.fruithub

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.ligerinc.fruithub.domain.Classification
import com.ligerinc.fruithub.domain.FruitClassifier

class FruitImageAnalyzer(
    private val classifier: FruitClassifier,
    private val onResult: (List<Classification>) -> Unit
): ImageAnalysis.Analyzer{

    private var frameSkipCounter = 0
    override fun analyze(image: ImageProxy) {
        if(frameSkipCounter%60 == 0){
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(100,100)
            val results = classifier.classify(bitmap,rotationDegrees)
            onResult(results)
        }
        frameSkipCounter++

        image.close()
    }
}