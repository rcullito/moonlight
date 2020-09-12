package com.example.moonlight.individual

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.database.SleepPositionDao

class IndividualViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  lateinit var positions: List<SleepPosition>

  fun getSpecificDate(date: String): List<SleepPosition> {
    positions = database.getSpecificDate(date, date)
    return positions
  }



}



