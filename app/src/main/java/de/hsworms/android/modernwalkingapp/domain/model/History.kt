package de.hsworms.android.modernwalkingapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "history")
data class History(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val walkType: String?,
    val walkTypeUnit: String?,
    val goal: Int?,  // 5 km
    val startTime: Date?,
    val endTime: Date?,
    val minAchieved: Int?,
    val stepsAchieved: Int?,
    val calBurned: Int?,
    val kmAchieved: Float?,
    val goalAchieved : Boolean?

    // time   min    20     x       y       15      xKal    yKM     false
)

