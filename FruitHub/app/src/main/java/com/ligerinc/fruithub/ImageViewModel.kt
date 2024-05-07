package com.ligerinc.fruithub

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageViewModel: ViewModel(){
    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())

    var capturedBitmaps by mutableStateOf<MutableMap<Int, Bitmap>>(mutableMapOf())
    val bitmap = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap){
        _bitmaps.value += bitmap
    }
}