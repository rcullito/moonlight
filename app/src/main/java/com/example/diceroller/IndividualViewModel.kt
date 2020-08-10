package com.example.diceroller

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.diceroller.database.SleepPosition
import com.example.diceroller.database.SleepPositionDao

class IndividualViewModel(val database: SleepPositionDao, application: Application, date:String): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  val positions = database.getSpecificDate(date, date)

  val positionsString = Transformations.map(positions) {
    formatPosition(it)
  }

}



