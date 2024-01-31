package de.hsworms.android.modernwalkingapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.hsworms.android.modernwalkingapp.domain.model.Profile


/*
* The DAO contains the SQL statements required by the repository to insert, retrieve and delete data within the SQLite database.
* These SQL statements are mapped to methods that are then called from within the repository to execute the corresponding query.
* */

@Dao
interface ProfileDao {

    @Insert
    suspend fun insert(profile: Profile)

    @Update
    suspend fun update(profile: Profile)

    @Delete
    suspend fun delete(profile: Profile)

    @Query("SELECT * From `profile` where id=:id")
    fun fetchProfileItemById(id: Int): List<Profile>

    @Query("SELECT * From `profile`")
    fun getAllProfiles(): LiveData<List<Profile>>
}