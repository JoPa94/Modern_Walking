package de.hsworms.android.modernwalkingapp.presentation

import android.app.Application
import android.util.Log
import android.util.Log.INFO
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.hsworms.android.modernwalkingapp.data.db.ProfileDatabase
import de.hsworms.android.modernwalkingapp.domain.model.Profile
import de.hsworms.android.modernwalkingapp.domain.repository.ProfileRepository
import java.util.logging.Level.INFO
import javax.inject.Inject


class ProfileViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: ProfileRepository
    val profiles : LiveData<List<Profile>>

    init {
        val profileDb = ProfileDatabase.getInstance(application)
        val profileDao = profileDb.profileDao()
        repository = ProfileRepository(profileDao)

        profiles = repository.profiles

    }

    fun createProfile(profile: Profile) {
        repository.createProfile(profile)
    }


    fun getProfile(): LiveData<List<Profile>> {
        return repository.profiles
    }

    fun updateProfile(profile: Profile){
        repository.updateProfile(profile)
    }







}