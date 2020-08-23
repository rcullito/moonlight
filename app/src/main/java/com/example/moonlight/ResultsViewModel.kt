package com.example.moonlight

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.moonlight.database.SleepPositionDao
import com.example.moonlight.network.DaphneApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultsViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  override fun onCleared() {
    super.onCleared()
  }

  val dates = database.getUniqueDates()

  fun getMarsRealEstateProperties() {
    DaphneApi.retrofitService.getProperties().enqueue(object : Callback<String> {
      override fun onFailure(call: Call<String>, t: Throwable) {
        val marsError = "Failure: " + t.message
        Log.i("Mars", marsError)
      }

      override fun onResponse(call: Call<String>, response: Response<String>) {
        Log.i("Mars", response.body())
      }
    })
  }

}
