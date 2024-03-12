package com.littlelemon.fruithub

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SwitchViewModel : ViewModel() {
    var switchState by mutableStateOf(false)
    var selectedIndex by mutableIntStateOf(0)

}