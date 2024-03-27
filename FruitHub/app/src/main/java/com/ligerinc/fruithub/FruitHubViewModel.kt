package com.ligerinc.fruithub

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FruitHubViewModel : ViewModel() {
    var switchStateExplore by mutableStateOf(false)
    var switchStateSaves by mutableStateOf(false)

}