package de.hsworms.android.modernwalkingapp.domain.repository

import androidx.lifecycle.LiveData
import de.hsworms.android.modernwalkingapp.data.db.HistoryDao
import de.hsworms.android.modernwalkingapp.domain.model.History
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HistoryRepository(private val historyDao: HistoryDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val histories: LiveData<List<History>> = historyDao.getAllHistories()


    fun insertHistoryItem(newHistoryItem: History) {
        coroutineScope.launch(Dispatchers.IO) {
            historyDao.insert(newHistoryItem)
        }
    }

    // for future updates
    fun deleteHistoryItem(historyItem: History) {
        coroutineScope.launch(Dispatchers.IO) {
            historyDao.delete(historyItem)
        }
    }

}