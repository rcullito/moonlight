package com.example.moonlight

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.moonlight.database.SleepPositionDao

class DateViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  private val dates = database.getUniqueDates()

}
