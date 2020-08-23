package com.example.moonlight.dates

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moonlight.database.SleepPositionDao

class ResultsViewModelFactory(private val dataSource: SleepPositionDao,
                             private val application: Application): ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
      return ResultsViewModel(
          dataSource,
          application
      ) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
