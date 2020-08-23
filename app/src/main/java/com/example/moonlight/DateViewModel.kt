package com.example.moonlight

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.moonlight.database.SleepPositionDao
import com.example.moonlight.network.DaphneApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DateViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  private val dates = database.getUniqueDates()

}
