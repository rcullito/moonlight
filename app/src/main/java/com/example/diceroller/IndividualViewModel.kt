package com.example.diceroller

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.diceroller.database.SleepPosition
import com.example.diceroller.database.SleepPositionDao

class IndividualViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  lateinit var positionsString: LiveData<Spanned>

  fun getSpecificDate(date: String): LiveData<Spanned> {
    var positions = database.getSpecificDate(date, date)

    positionsString = Transformations.map(positions) {
      formatPosition(it)
    }

    return positionsString
  }



}



