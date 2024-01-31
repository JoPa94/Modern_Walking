package de.hsworms.android.modernwalkingapp.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.delay
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class WalkViewModel (application: Application) : AndroidViewModel(application){

    private val locationLiveData = LocationLiveData(application)

    fun getLocationLiveData() = locationLiveData

    var lastLat : Double = 0.0
    var lastLon : Double = 0.0

    var lat : Double = 49.6341
    var lon : Double = 8.3507

    var distance: Float = 0f

    var stepsAchieved by mutableStateOf(0)
    var minAchieved by mutableStateOf(0)
    var calBurned by mutableStateOf(0)
    var kmAchieved by mutableStateOf(0f)
    var kmInMin by mutableStateOf("-")
    var bmp by mutableStateOf("-")
    var startDate: Date = Date()
    var minutes by mutableStateOf(0)
    var seconds by mutableStateOf(0)


    fun startLocationUpdates() {
        locationLiveData.startLocationUpdates()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun startTimer(onTimerEnd: () -> Unit) {
        while(true) {
            delay(1.seconds)
            minAchieved++
            minutes = minAchieved / 60
            seconds = minAchieved % 60
        }
    }

}