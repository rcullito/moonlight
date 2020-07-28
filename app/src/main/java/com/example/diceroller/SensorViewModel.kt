package com.example.diceroller

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.diceroller.workers.SensorWorker
import java.util.*
import java.util.concurrent.TimeUnit

class SensorViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

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

  internal fun startTiltSensor() {
    Log.i("SensorViewModel", "startTiltSensor() called")
    val sensorManager =  getApplication<Application>().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    sensorManager.getDefaultSensor(22)?.also { tiltSensor ->
      sensorManager.registerListener(
        this,
        tiltSensor,
        SensorManager.SENSOR_DELAY_NORMAL,
        SensorManager.SENSOR_DELAY_NORMAL
      )
    }

  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

  override fun onSensorChanged(event: SensorEvent?) {
    Log.i("Tilt", "titl")
  }

  fun cancelWork() {
    Log.i("SensorViewModel", "cancelWork function called")
    workManager.cancelWorkById(singleWorkRequestId)
  }

  fun isSensorWorkInfoInitialised(): Boolean {
    return ::sensorWorkInfo.isInitialized
  }

}
