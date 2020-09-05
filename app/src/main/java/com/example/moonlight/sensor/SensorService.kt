package com.example.moonlight.sensor

import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.moonlight.*
import com.example.moonlight.database.SleepDatabase
import com.example.moonlight.database.SleepPosition
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

  fun tearDownListenerAndAssoc() {
    Log.i("SensorService", "unregistering sensor listener")
    sensorManager.unregisterListener(this)

    wakelock.release()
  }

  fun fireUpAndAssoc() {
    database = SleepDatabase.getInstance(applicationContext)

    Log.i("SensorService", "oncreate that we just put in place")
    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).also { accelerometer ->
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
    //do heavy work on a background thread

    if (intent != null) {
      if(intent.action.equals(pauseAction))
        tearDownListenerAndAssoc();

      if(intent.action.equals(startAction))
        fireUpAndAssoc();

      if(intent.action.equals(stopAction))
        // TODO having issues here with the wakelock
        stopSelf();
    }

    var notification = buildNotification(intent, applicationContext)


    startForeground(1, notification)
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

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onSensorChanged(event: SensorEvent) {
      if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
        System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
      } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
        System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
      }
      // TODO make this a function in Utils namespace
      // log different time capabilities here
      var elapsedRealTimeMillis = SystemClock.elapsedRealtime() // Returns milliseconds since boot, including time spent in sleep.

      var eventElapsedRealTimeMillis = TimeUnit.NANOSECONDS.toMillis(event.timestamp) // event.timestamp will return nanoseconds since boot, including time spent in sleep.

      //  evaluate the difference between now() and the event with the shared backdrop of milliseconds since boot, including sleep
      var timeSinceEventMillis = elapsedRealTimeMillis - eventElapsedRealTimeMillis
      // Log.i("timeSinceEvent", timeSinceEventMillis.toString())

      var currentClockTime = System.currentTimeMillis()

      var eventClockTime = currentClockTime - timeSinceEventMillis

      // TODO allow for this to maybe return a value and only perform work if there is a value
      var mostRecentPosition = updateOrientationAngles(accelerometerReading, magnetometerReading, eventClockTime, applicationContext)

      workerScope.launch {

        withContext(Dispatchers.IO) {
          updatePositionInDb(mostRecentPosition)
        }
      }

  }

  private fun updatePositionInDb(position:SleepPosition) {
    database.sleepPositionDao.insert(position)
  }
}
