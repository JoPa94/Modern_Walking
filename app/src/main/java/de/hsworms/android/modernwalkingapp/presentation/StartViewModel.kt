package de.hsworms.android.modernwalkingapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class StartViewModel : ViewModel() {

    var goalCounter by mutableStateOf(3)
    var startingType = mutableStateOf("km")
    var walkType = mutableStateOf("distance")

    fun increaseGoalCounter() {
        goalCounter++
    }

    fun decreaseGoalCounter() {
        if (goalCounter > 1) {
            goalCounter--
        }
    }


}