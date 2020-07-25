package com.example.diceroller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.example.diceroller.database.SleepPositionDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ResultsViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  private var viewModelJob = Job()
  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }

  private val positions = database.getAllPositions()

  val positionsString = Transformations.map(positions) {
    formatPosition(it)
  }


}
