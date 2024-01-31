package de.hsworms.android.modernwalkingapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.hsworms.android.modernwalkingapp.domain.model.History

@Database(entities = [History::class], version = 2, exportSchema = false)
@TypeConverters(HistoryTypeConverter::class)
abstract class HistoryDatabase: RoomDatabase() {

    abstract fun histroyDao(): HistoryDao

    companion object {

        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase {

            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        "history_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}