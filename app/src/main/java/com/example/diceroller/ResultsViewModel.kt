package com.example.diceroller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.Navigation
import com.example.diceroller.database.SleepPositionDao

class ResultsViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  val dates = database.getUniqueDates()

}
