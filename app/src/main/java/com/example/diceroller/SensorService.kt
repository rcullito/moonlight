package com.example.diceroller

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SensorService: Service() {

  // only called once in the lifecycle
  override fun onCreate() {
    super.onCreate()
  }

  // called every time we trigger it
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onDestroy() {
    super.onDestroy()
  }

}
