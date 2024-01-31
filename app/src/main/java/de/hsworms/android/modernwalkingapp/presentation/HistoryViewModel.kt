package de.hsworms.android.modernwalkingapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import de.hsworms.android.modernwalkingapp.data.db.HistoryDatabase
import de.hsworms.android.modernwalkingapp.domain.model.History
import de.hsworms.android.modernwalkingapp.domain.repository.HistoryRepository
import java.util.*

class HistoryViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository
    val histories : LiveData<List<History>>

    init {
        val historyDb = HistoryDatabase.getInstance(application)
        val historyDao = historyDb.histroyDao()
        repository = HistoryRepository(historyDao)

        histories = repository.histories


        // dummy data for history
//        val historyItem1 = History(id = 0, walkType = "time", walkTypeUnit = "min",
//            goal = 23, startTime = Date(), endTime = Date(), minAchieved = 25,  stepsAchieved = 1055, calBurned = 100, kmAchieved = 3.6F, goalAchieved = true)
//        val historyItem2 = History(id = 0, walkType = "distance", walkTypeUnit = "km",
//            goal = 16, startTime = Date(), endTime = Date(), minAchieved = 5, stepsAchieved = 1400, calBurned = 130, kmAchieved = 10F, goalAchieved = false)
//
//        val historyItem3 = History(id = 0, walkType = "cal", walkTypeUnit = "cal",
//            goal = 5, startTime = Date(), endTime = Date(), minAchieved = 15,  stepsAchieved = 800, calBurned = 156, kmAchieved = 6.3F, goalAchieved = true)
//
//        val historyItem4 = History(id = 0, walkType = "time", walkTypeUnit = "min",
//            goal = 40, startTime = Date(), endTime = Date(), minAchieved = 20,  stepsAchieved = 950, calBurned = 98, kmAchieved = 7.9F, goalAchieved = false)
//
//        repository.insertHistoryItem(historyItem1)
//        repository.insertHistoryItem(historyItem2)
//        repository.insertHistoryItem(historyItem3)
//        repository.insertHistoryItem(historyItem4)

    }

    fun insertNewHistoryItem(history: History) {
        repository.insertHistoryItem(history)
    }




}