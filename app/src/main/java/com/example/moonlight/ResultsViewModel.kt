package com.example.moonlight

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.moonlight.database.SleepPositionDao

class ResultsViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  val dates = database.getUniqueDates()

}
