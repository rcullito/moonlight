package com.example.diceroller

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diceroller.database.SleepPositionDao

class IndividualViewModelFactory(private val dataSource: SleepPositionDao,
                              private val application: Application, private val date: String): ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(IndividualViewModel::class.java)) {
      return IndividualViewModel(dataSource, application, date) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
