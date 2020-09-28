package com.example.moonlight.sensor

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.moonlight.database.SleepDatabase
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.notificationId
import com.example.moonlight.pauseAction
import com.example.moonlight.startAction
import com.example.moonlight.stopAction
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class SensorService : Service(), SensorEventListener {
  private lateinit var sensorManager: SensorManager
  private val accelerometerReading = FloatArray(3)
  private val magnetometerReading = FloatArray(3)
  private var workerJob = Job()
  private val workerScope = CoroutineScope(Dispatchers.Default + workerJob)
  private lateinit var database: SleepDatabase
  lateinit var wakelock: PowerManager.WakeLock
  private var lastUpdate: Long = 0

  companion object {
    fun startService(context: Context, message: String) {
      val startIntent = Intent(context, SensorService::class.java)
      startIntent.putExtra("inputExtra", message)
      ContextCompat.startForegroundService(context, startIntent)
    }
    fun cancelService(context: Context) {
      val stopIntent = Intent(context, SensorService::class.java)
      context.stopService(stopIntent)
    }
  }

  private fun updatePositionInDb(position:SleepPosition) {
    database.sleepPositionDao.insert(position)
  }

  fun updateSleepPositionOnSensorChanged(mostRecentPosition: SleepPosition) {
    workerScope.launch {
      withContext(Dispatchers.IO) {
        updatePositionInDb(mostRecentPosition)
      }
    }
  }

  fun tearDownListenerAndAssoc() {
    Log.i("SensorService", "unregistering sensor listener")
    sensorManager.unregisterListener(this)

    wakelock.release()
  }

  fun fireUpAndAssoc() {
    database = SleepDatabase.getInstance(applicationContext)
    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).also { accelerometer ->
      Log.i("SensorService", "registering sensor listener")
      sensorManager.registerListener(
        this,
        accelerometer,
        SensorManager.SENSOR_DELAY_NORMAL,
        // When maxReportLatencyUs is 0 it requires the events to be delivered as soon as possible
        0
      )
    }
    sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).also { magneticField ->
      sensorManager.registerListener(
        this,
        magneticField,
        SensorManager.SENSOR_DELAY_NORMAL,
        0
      )
    }

    wakelock =
      (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
        newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
          acquire()
        }
      }
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

    Log.i("SensorService", "onStart called")

    if (intent != null) {
      if(intent.action.equals(pauseAction)) {
        Log.i("SensorService", "pausing")
        tearDownListenerAndAssoc();
      }

      if(intent.action.equals(startAction)) {
        Log.i("SensorService", "starting")
        fireUpAndAssoc();
      }

      if(intent.action.equals(stopAction)) {
        // TODO having issues here with the wakelock
        Log.i("SensorService", "stopping")
        stopSelf();
      }
    }

    var notification = buildNotification(intent, applicationContext)

    startForeground(notificationId, notification)
    return START_STICKY
  }


  override fun onDestroy() {
    super.onDestroy()
    tearDownListenerAndAssoc()
  }

  override fun onCreate() {
    super.onCreate()
    fireUpAndAssoc()
  }


  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

  fun deriveEventClockTime(eventTimeStamp: Long): Long {
    // event time relative to system boot
    var timeSinceEventMillis = SystemClock.elapsedRealtime() - TimeUnit.NANOSECONDS.toMillis(eventTimeStamp)
    // when did our event happen in a wall clock sense
    return System.currentTimeMillis() - timeSinceEventMillis
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onSensorChanged(event: SensorEvent) {
      if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
        System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
      } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
        System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
      }

    var eventClockTime = deriveEventClockTime(event.timestamp)

    if ((eventClockTime - lastUpdate) > 1000) {
      lastUpdate = eventClockTime
      var mostRecentPosition = updateOrientationAngles(accelerometerReading, magnetometerReading, eventClockTime, applicationContext) // 1.
      updateSleepPositionOnSensorChanged(mostRecentPosition)
    }

  }

}
