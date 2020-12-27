package com.robertculliton.moonlight.individual

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.robertculliton.moonlight.database.SleepPosition
import com.robertculliton.moonlight.database.SleepPositionDao

class IndividualViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  lateinit var positions: LiveData<List<SleepPosition>>

  fun getSpecificDate(date: String) {
    positions = database.getSpecificDate(date, date)
  }

}



