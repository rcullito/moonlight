package com.example.diceroller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.example.diceroller.database.SleepPositionDao

class IndividualViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  val positions = database.getSpecificDate("2020-08-06", "2020-08-08")

  val positionsString = Transformations.map(positions) {
    formatPosition(it)
  }
}



