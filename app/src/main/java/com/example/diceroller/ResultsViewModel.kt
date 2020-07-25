package com.example.diceroller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.diceroller.database.SleepPosition
import com.example.diceroller.database.SleepPositionDao
import kotlinx.coroutines.*

class ResultsViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  private var viewModelJob = Job()
  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }


  // TODO these functions are for inserting records, but we want to make sure that we are retrieving records for this page

  /*
  fun savePosition(sleepPosition: SleepPosition) {
    uiScope.launch {
      insert(sleepPosition)
    }
  }

  private suspend fun insert(sleepPosition: SleepPosition) {
    withContext(Dispatchers.IO) {
      database.insert(sleepPosition)
    }
  }

  */

}
