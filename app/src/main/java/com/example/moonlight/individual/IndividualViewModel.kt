package com.example.moonlight.individual

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.database.SleepPositionDao
import kotlinx.coroutines.launch

class IndividualViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  lateinit var positions: LiveData<List<SleepPosition>>

  fun getSpecificDate(date: String) {
    positions = database.getSpecificDate(date, date)
  }

}



