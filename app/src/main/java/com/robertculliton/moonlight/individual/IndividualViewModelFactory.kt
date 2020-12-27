package com.robertculliton.moonlight.individual

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.robertculliton.moonlight.database.SleepPositionDao

class IndividualViewModelFactory(private val dataSource: SleepPositionDao,
                              private val application: Application): ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(IndividualViewModel::class.java)) {
      return IndividualViewModel(
          dataSource,
          application
      ) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
