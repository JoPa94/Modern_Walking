package de.hsworms.android.modernwalkingapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String = "",
    var age: Int,
    var height: Int,
    var weight: Int,
    var gender: Int,
    var xp: Int,  // experience points
    var bio: String,
    var img: String
)



