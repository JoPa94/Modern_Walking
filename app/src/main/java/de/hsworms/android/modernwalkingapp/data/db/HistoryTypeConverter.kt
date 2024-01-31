package de.hsworms.android.modernwalkingapp.data.db

import androidx.room.TypeConverter
import java.util.*

class HistoryTypeConverter {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }
}