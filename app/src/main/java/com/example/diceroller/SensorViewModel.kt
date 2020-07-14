package com.example.diceroller

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.diceroller.workers.LogWorker
import java.util.concurrent.TimeUnit

class SensorViewModel(application: Application) : AndroidViewModel(application) {


  private val workManager = WorkManager.getInstance(application)

  internal fun startLogWorker() {
    Log.i("SensorViewModel", "Starting the LogWorker" )

    val logRequest =
      PeriodicWorkRequestBuilder<LogWorker>(1, TimeUnit.MINUTES)
        // Additional configuration
        .build()

    workManager.enqueue(logRequest)
  }
}
