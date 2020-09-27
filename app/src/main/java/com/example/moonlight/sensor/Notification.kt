package com.example.moonlight.sensor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.moonlight.R
import com.example.moonlight.pauseAction
import com.example.moonlight.startAction
import com.example.moonlight.stopAction

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

// if we are starting, show pause and stop
// if we are pausing, show start and stop

private var icon1: Int = 0
private lateinit var title1: String
private lateinit var intent1: PendingIntent

fun buildNotification(intent: Intent?, ctx: Context): Notification {
  val input = intent?.getStringExtra("inputExtra")
  createNotificationChannel(ctx)

  val pausePendingIntent = buildPendingIntent(pauseAction, ctx)
  val playPendingIntent = buildPendingIntent(startAction, ctx)
  val stopPendingIntent = buildPendingIntent(stopAction, ctx)

  if(intent?.action.equals(pauseAction)) {
    icon1 = R.drawable.ic_play_arrow_black_24dp
    title1 = "Play"
    intent1 = playPendingIntent
  } else {
    icon1 = R.drawable.ic_pause_black_24dp
    title1 = "Pause"
    intent1 = pausePendingIntent
  }


  val notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    .setContentTitle("Vibrate")
    .setContentText(input)
    .setSmallIcon(R.drawable.ic_stat_player)
    .addAction(icon1, title1, intent1)
    .addAction(R.drawable.ic_baseline_stop_24, "Pause", stopPendingIntent)
    .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
      .setShowActionsInCompactView(0, 1))
    .build()

  return notification

}







