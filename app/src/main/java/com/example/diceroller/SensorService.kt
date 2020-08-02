package com.example.diceroller

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.diceroller.database.SleepDatabase
import com.example.diceroller.database.SleepPosition
import kotlinx.coroutines.*

class SensorService : Service(), SensorEventListener {
  private val CHANNEL_ID = "ForegroundService Kotlin"
  private lateinit var sensorManager: SensorManager
  private val accelerometerReading = FloatArray(3)
  private val magnetometerReading = FloatArray(3)
  val rotationMatrix = FloatArray(9)
  val orientationAngles = FloatArray(3)
  private var lastUpdate: Long = 0
  private var workerJob = Job()
  private val workerScope = CoroutineScope(Dispatchers.Default + workerJob)
  private lateinit var database: SleepDatabase

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
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    //do heavy work on a background thread
    val input = intent?.getStringExtra("inputExtra")
    createNotificationChannel()
    val notificationIntent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
      this,
      0, notificationIntent, 0
    )
    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("Foreground Service Kotlin Example")
      .setContentText(input)
      .setContentIntent(pendingIntent)
      .build()
    startForeground(1, notification)
    //stopSelf();
    // TODO revisit what should go here
    return START_STICKY
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.i("SensorService", "unregistering sensor listener")
    sensorManager.unregisterListener(this)
  }

  override fun onCreate() {
    super.onCreate()

    database = SleepDatabase.getInstance(applicationContext)

    Log.i("SensorService", "oncreate that we just put in place")
    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).also { accelerometer ->
      sensorManager.registerListener(
        this,
        accelerometer,
        SensorManager.SENSOR_DELAY_NORMAL,
        SensorManager.SENSOR_DELAY_NORMAL
      )
    }
    sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).also { magneticField ->
      sensorManager.registerListener(
        this,
        magneticField,
        SensorManager.SENSOR_DELAY_NORMAL,
        SensorManager.SENSOR_DELAY_NORMAL
      )
    }


  }

  override fun onBind(intent: Intent): IBinder? {
    return null
  }
  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
        NotificationManager.IMPORTANCE_DEFAULT)
      val manager = getSystemService(NotificationManager::class.java)
      manager!!.createNotificationChannel(serviceChannel)
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

  override fun onSensorChanged(event: SensorEvent) {
      if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
        System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
      } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
        System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
      }
      updateOrientationAngles(accelerometerReading, magnetometerReading)
  }

  fun updateOrientationAngles(bananas: FloatArray, coconuts: FloatArray) {
    SensorManager.getRotationMatrix(rotationMatrix, null, bananas, coconuts)
    SensorManager.getOrientation(rotationMatrix, orientationAngles)
    var azimuth = orientationAngles.get(0)
    var pitch = orientationAngles.get(1)
    var roll = orientationAngles.get(2)

    // cross yourself
    if (azimuth.toDouble() != 0.0 || pitch.toDouble() != 0.0 || roll.toDouble() != 0.0) {
      var currentTime = System.currentTimeMillis()

      if ((currentTime - lastUpdate) > (60000 * 10)) {

        Log.i("SensorWorker/azimuth", azimuth.toString())
        Log.i("SensorWorker/pitch", pitch.toString())
        Log.i("SensorWorker/roll", roll.toString())

        // write to database
        val position = SleepPosition(pitch = pitch, roll = roll)

        workerScope.launch {

          withContext(Dispatchers.IO) {
            updatePositionInDb(position)
          }
        }
        lastUpdate = currentTime
      }
    }
  }

  private suspend fun updatePositionInDb(position:SleepPosition) {
    database.sleepPositionDao.insert(position)
  }
}
