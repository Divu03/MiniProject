package com.ligerinc.fruithub.domain

import android.graphics.Bitmap

interface FruitClassifier {
    fun classify(bitmap: Bitmap,rotation: Int): List<Classification>
}