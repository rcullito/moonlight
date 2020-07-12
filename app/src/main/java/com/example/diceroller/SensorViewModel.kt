package com.example.diceroller

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.diceroller.workers.LogWorker

class SensorViewModel(application: Application) : AndroidViewModel(application) {


  private val workManager = WorkManager.getInstance(application)

  internal fun startLogWorker() {
    Log.i("SensorViewModel", "Starting the LogWorker" )
    workManager.enqueue(OneTimeWorkRequest.from(LogWorker::class.java))
  }
}
