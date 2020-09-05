package com.example.moonlight.sensor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

private val CHANNEL_ID = "ForegroundService Kotlin"

private fun createNotificationChannel(ctx: Context) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
      NotificationManager.IMPORTANCE_DEFAULT)
    val manager = ctx.getSystemService(NotificationManager::class.java)
    manager!!.createNotificationChannel(serviceChannel)
  }
}
