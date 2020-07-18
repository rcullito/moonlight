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
import java.util.*
import java.util.concurrent.TimeUnit

class SensorViewModel(application: Application) : AndroidViewModel(application) {


  private val workManager = WorkManager.getInstance(application)
  // internal val sensorWorkInfo: LiveData<WorkInfo>
  private lateinit var singleWorkRequestId: UUID

  init {
    // sensorWorkInfo = workManager.getWorkInfoByIdLiveData(singleWorkRequestId)
  }

  internal fun startLogWorker() {
    Log.i("SensorViewModel", "Starting the LogWorker" )

    val sensorRequest =
      PeriodicWorkRequestBuilder<SensorWorker>(16, TimeUnit.MINUTES)
        // Additional configuration
        .build()
    Log.i("SensorViewModel", "getting the potentially unique workRequest Id")
    Log.i("SensorViewModel", sensorRequest.id.toString())
    singleWorkRequestId = sensorRequest.id

    workManager.enqueue(sensorRequest)
  }

  fun cancelWork() {
    Log.i("SensorViewModel", "cancelWork function called")
    workManager.cancelWorkById(singleWorkRequestId)
  }

}
