package de.hsworms.android.modernwalkingapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.hsworms.android.modernwalkingapp.domain.model.History

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(history: History)

    @Update
    suspend fun update(history: History)

    @Delete
    suspend fun delete(history: History)

    @Query("SELECT * From `history` where id=:id")
    fun fetchHistoryItemById(id: Int): List<History>

    @Query("SELECT * From `history`")
    fun getAllHistories(): LiveData<List<History>>

}