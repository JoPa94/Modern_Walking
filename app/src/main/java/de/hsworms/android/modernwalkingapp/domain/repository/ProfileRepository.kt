package de.hsworms.android.modernwalkingapp.domain.repository

import androidx.lifecycle.LiveData
import de.hsworms.android.modernwalkingapp.domain.model.Profile
import de.hsworms.android.modernwalkingapp.data.db.ProfileDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/*
* The repository module contains all of the code necessary for directly handling all data sources used by the app.
* This avoids the need for the UI controller and ViewModel to contain code that directly accesses sources
* such as databases or web services.
*
* The repository class will be responsible for interacting with the Room database on behalf of the ViewModel and
* will need to provide methods that use the DAO to insert, delete and query product records.
* */
class ProfileRepository (private val profileDao: ProfileDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // for the future => multiple profiles
    val profiles: LiveData<List<Profile>> = profileDao.getAllProfiles()

    fun createProfile(profile: Profile) {
        coroutineScope.launch(Dispatchers.IO) {
            profileDao.insert(profile)
        }
    }

    fun updateProfile(profile: Profile) {
        coroutineScope.launch(Dispatchers.IO) {
            profileDao.update(profile)
        }
    }
}