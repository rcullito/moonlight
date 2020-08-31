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
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.moonlight.MainActivity
import com.example.moonlight.R
import com.example.moonlight.database.SleepDatabase
import com.example.moonlight.database.SleepPosition
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

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
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    //do heavy work on a background thread


    if (intent != null) {
      // TODO keep the service running but remove the listener
      // we now need to decouple these 2 things as we'd like to have the foreground service running
      // without the phone vibrating
      if(intent.action.equals("PAUSE"))
        tearDownListenerAndAssoc();

      if(intent.action.equals("START"))
        fireUpAndAssoc();

      if(intent.action.equals("STOP"))
        stopSelf();
    }


    val input = intent?.getStringExtra("inputExtra")
    createNotificationChannel()



    var lambda = {action: String ->
      var scopedIntent = Intent(this, SensorService::class.java).setAction(action)
      PendingIntent.getService(this, 0, scopedIntent, PendingIntent.FLAG_CANCEL_CURRENT)
    }
    // todo make pause, start, and stop consts
    val pausePendingIntent = lambda("PAUSE")
    val playPendingIntent = lambda("START")
    val stopPendingIntent = lambda("STOP")

    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
      .setContentTitle("Vibrate When Outside of Desired Range")
      .setContentText(input)
      .setSmallIcon(R.drawable.ic_stat_player)
      .addAction(R.drawable.ic_play_arrow_black_24dp, "Play", playPendingIntent) // #0
      .addAction(R.drawable.ic_pause_black_24dp, "Pause", pausePendingIntent) // #1
      .addAction(R.drawable.ic_album_black_24dp, "Pause", stopPendingIntent) // #2
      .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
        .setShowActionsInCompactView(0))
      .build()


    startForeground(1, notification)
    //stopSelf();
    // TODO revisit what should go here
    return START_STICKY
  }

  fun tearDownListenerAndAssoc() {
    Log.i("SensorService", "unregistering sensor listener")
    sensorManager.unregisterListener(this)

    wakelock.release()
  }

  override fun onDestroy() {
    super.onDestroy()
    tearDownListenerAndAssoc()
  }

  override fun onCreate() {
    super.onCreate()
    fireUpAndAssoc()
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

      // log different time capabilities here
      var elapsedRealTimeMillis = SystemClock.elapsedRealtime() // Returns milliseconds since boot, including time spent in sleep.

      var eventElapsedRealTimeMillis = TimeUnit.NANOSECONDS.toMillis(event.timestamp) // event.timestamp will return nanoseconds since boot, including time spent in sleep.

      //  evaluate the difference between now() and the event with the shared backdrop of milliseconds since boot, including sleep
      var timeSinceEventMillis = elapsedRealTimeMillis - eventElapsedRealTimeMillis
      // Log.i("timeSinceEvent", timeSinceEventMillis.toString())

      var currentClockTime = System.currentTimeMillis()

      var eventClockTime = currentClockTime - timeSinceEventMillis

      updateOrientationAngles(accelerometerReading, magnetometerReading, eventClockTime)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun updateOrientationAngles(bananas: FloatArray, coconuts: FloatArray, eventTimestamp: Long) {
    SensorManager.getRotationMatrix(rotationMatrix, null, bananas, coconuts)
    SensorManager.getOrientation(rotationMatrix, orientationAngles)
    var azimuth = orientationAngles.get(0)
    var pitch = orientationAngles.get(1)
    var roll = orientationAngles.get(2)

    if (abs(roll.toDouble()) < 2.34) {
      val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
      val effect: VibrationEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
      vibrator.vibrate(effect)
    }

    // cross yourself
    if (azimuth.toDouble() != 0.0 || pitch.toDouble() != 0.0 || roll.toDouble() != 0.0) {
      var currentEventTime = eventTimestamp

      if ((currentEventTime - lastUpdate) > (1000)) {

        Log.i("SensorWorker/azimuth", azimuth.toString())
        Log.i("SensorWorker/pitch", pitch.toString())
        Log.i("SensorWorker/roll", roll.toString())

        // write to database
        val position = SleepPosition(pitch = pitch, roll = roll, sleepPositionTime = eventTimestamp)

        workerScope.launch {

          withContext(Dispatchers.IO) {
            updatePositionInDb(position)
          }
        }
        lastUpdate = currentEventTime
      }
    }
  }

  private fun updatePositionInDb(position:SleepPosition) {
    database.sleepPositionDao.insert(position)
  }
}
