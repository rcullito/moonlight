package com.robertculliton.moonlight.sensor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.robertculliton.moonlight.R
import com.robertculliton.moonlight.stopAction

private val CHANNEL_ID = "ForegroundService Kotlin"

var buildPendingIntent = {action: String, ctx: Context ->
  var scopedIntent = Intent(ctx, SensorService::class.java).setAction(action)
  PendingIntent.getService(ctx, 0, scopedIntent, PendingIntent.FLAG_CANCEL_CURRENT)
}

fun createNotificationChannel(ctx: Context) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
      NotificationManager.IMPORTANCE_DEFAULT)
    val manager = ctx.getSystemService(NotificationManager::class.java)
    manager!!.createNotificationChannel(serviceChannel)
  }
}



fun buildNotification(intent: Intent?, ctx: Context): Notification {
  val input = intent?.getStringExtra("inputExtra")
  createNotificationChannel(ctx)

  val stopPendingIntent = buildPendingIntent(stopAction, ctx)

  val notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    .setContentText(input)
    .setSmallIcon(R.drawable.ic_stat_player)
    .addAction(R.drawable.ic_baseline_stop_24, "Pause", stopPendingIntent)
    .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
      .setShowActionsInCompactView(0))
    .build()

  return notification

}







