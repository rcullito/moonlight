package com.example.diceroller

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.diceroller.workers.LogWorker
import com.example.diceroller.workers.SensorWorker
import java.util.concurrent.TimeUnit

class SensorViewModel(application: Application) : AndroidViewModel(application) {


  private val workManager = WorkManager.getInstance(application)
  internal val sensorWorkInfos: LiveData<List<WorkInfo>>
  val sixteenTag: String = "16MinuteSensor"

  init {
   sensorWorkInfos = workManager.getWorkInfosByTagLiveData(sixteenTag)
  }

  internal fun startLogWorker() {
    Log.i("SensorViewModel", "Starting the LogWorker" )

    val sensorRequest =
      PeriodicWorkRequestBuilder<SensorWorker>(16, TimeUnit.MINUTES)
        // Additional configuration
        .addTag(sixteenTag)
        .build()

    workManager.enqueue(sensorRequest)
  }

  fun cancelWork() {
    Log.i("SensorViewModel", "cancelWork function called")
    workManager.cancelAllWorkByTag(sixteenTag)
  }

}
