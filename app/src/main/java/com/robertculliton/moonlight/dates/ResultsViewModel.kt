package com.robertculliton.moonlight.dates

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.robertculliton.moonlight.database.SleepPositionDao

class ResultsViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  val dates = database.getUniqueDates()

  // TODO we may also need start and end times from here

}
