package com.example.moonlight.workers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.PowerManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moonlight.database.SleepDatabase
import com.example.moonlight.database.SleepPosition
import kotlinx.coroutines.*

class SensorWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params), SensorEventListener {

  private lateinit var sensorManager: SensorManager
  private val geoMagneticReading = FloatArray(4)
  val database = SleepDatabase.getInstance(applicationContext)
  private var workerJob = Job()
  private val workerScope = CoroutineScope(Dispatchers.Default + workerJob)
  private lateinit var wakeLock: PowerManager.WakeLock


  override suspend fun doWork(): Result {
    Log.i("SensorWorker", "in the doWork() function")

   wakeLock =
      (applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
          newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Lupin::sensorWorkerWakeLock").apply {
          acquire()
        }
      }

    sensorManager = this.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)?.also { geoMagnetic ->
      sensorManager.registerListener(
        this,
        geoMagnetic,
        SensorManager.SENSOR_DELAY_NORMAL,
        SensorManager.SENSOR_DELAY_NORMAL
      )
    }

    return Result.success()
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

  override fun onSensorChanged(event: SensorEvent) {
    Log.i("SensorWorker", "onSensorChange fired")
    if (event.sensor.type == Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) {
      System.arraycopy(event.values, 0, geoMagneticReading, 0, geoMagneticReading.size)
    }

    updateWithGeomagenticValues()

  }

  fun updateWithGeomagenticValues() {

    var azimuth = geoMagneticReading.get(0)
    var pitch = geoMagneticReading.get(1)
    var roll = geoMagneticReading.get(2)

    Log.i("SensorWorker/azimuth", azimuth.toString())
    Log.i("SensorWorker/pitch", pitch.toString())
    Log.i("SensorWorker/roll", roll.toString())

    // cross yourself
    if (azimuth.toDouble() != 0.0 || pitch.toDouble() != 0.0 || roll.toDouble() != 0.0) {

      // write to database
      val position = SleepPosition(pitch = pitch, roll = roll, sleepPositionTime = System.currentTimeMillis())

      workerScope.launch {

        withContext(Dispatchers.IO) {
          updatePositionInDb(position)
        }
      }

      Log.i("SensorWorker", "unregistering sensor listener")
      sensorManager.unregisterListener(this)
      wakeLock.release()
    }

  }

  private suspend fun updatePositionInDb(position:SleepPosition) {
    database.sleepPositionDao.insert(position)
  }
}
