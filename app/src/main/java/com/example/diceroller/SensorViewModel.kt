package com.example.diceroller

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.diceroller.database.SleepPositionDao
import com.example.diceroller.workers.SensorWorker
import java.util.*
import java.util.concurrent.TimeUnit

class SensorViewModel(val database: SleepPositionDao, application: Application) : AndroidViewModel(application) {

  private val workManager = WorkManager.getInstance(application)
  lateinit var sensorWorkInfo: LiveData<List<WorkInfo>>
  private lateinit var singleWorkRequestId: UUID

  internal fun startSensorWorker() {

    val sensorRequest =
      PeriodicWorkRequestBuilder<SensorWorker>(16, TimeUnit.MINUTES)
        // Additional configuration
        .build()
    Log.i("SensorViewModel", "upcoming workRequest Id: ")
    Log.i("SensorViewModel", sensorRequest.id.toString())
    singleWorkRequestId = sensorRequest.id

    // this trial did block repeat work from being enqueued
    workManager.enqueueUniquePeriodicWork("ravenclaw", ExistingPeriodicWorkPolicy.KEEP, sensorRequest)

    sensorWorkInfo = workManager.getWorkInfosForUniqueWorkLiveData("ravenclaw")
  }

  fun cancelWork() {
    Log.i("SensorViewModel", "cancelWork function called")
    workManager.cancelWorkById(singleWorkRequestId)
  }

  fun isSensorWorkInfoInitialised(): Boolean {
    return ::sensorWorkInfo.isInitialized
  }

}
