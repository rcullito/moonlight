package com.example.diceroller.workers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.diceroller.database.SleepDatabase
import com.example.diceroller.database.SleepPosition
import com.example.diceroller.database.SleepPositionDao
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.abs

class SensorWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params), SensorEventListener {

  private lateinit var sensorManager: SensorManager
  private val accelerometerReading = FloatArray(3)
  private val magnetometerReading = FloatArray(3)
  val rotationMatrix = FloatArray(9)
  val orientationAngles = FloatArray(3)

  val database = SleepDatabase.getInstance(applicationContext)

  private var workerJob = Job()
  private val workerScope = CoroutineScope(Dispatchers.Default + workerJob)

  override suspend fun doWork(): Result {
    Log.i("SensorWorker", "in the doWork() function")

    sensorManager = this.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
      sensorManager.registerListener(
        this,
        accelerometer,
        SensorManager.SENSOR_DELAY_NORMAL,
        SensorManager.SENSOR_DELAY_NORMAL
      )
    }
    sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
      sensorManager.registerListener(
        this,
        magneticField,
        SensorManager.SENSOR_DELAY_NORMAL,
        SensorManager.SENSOR_DELAY_NORMAL
      )
    }
    // TODO run this once, and if it doesn't work
    // the name of the game is async and a listenable worker
    // ideally we would return when we get 3 values that are not 0
    return Result.success()
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

  override fun onSensorChanged(event: SensorEvent) {
    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
      System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
    } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
      System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
    }

    updateOrientationAngles()

  }

  fun updateOrientationAngles() {
    SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
    SensorManager.getOrientation(rotationMatrix, orientationAngles)
    var azimuth = orientationAngles.get(0)
    var pitch = orientationAngles.get(1)
    var roll = orientationAngles.get(2)

    Log.i("SensorWorker/azimuth", azimuth.toString())
    Log.i("SensorWorker/pitch", pitch.toString())
    Log.i("SensorWorker/roll", roll.toString())

    // cross yourself
    if (azimuth.toDouble() != 0.0 || pitch.toDouble() != 0.0 || roll.toDouble() != 0.0) {

      // write to database
      val position = SleepPosition(pitch = pitch, roll = roll)

      workerScope.launch {

        withContext(Dispatchers.IO) {
          updatePositionInDb(position)
        }
      }

      Log.i("SensorWorker", "unregistering sensor listener")
      sensorManager.unregisterListener(this)
    }

  }

  private suspend fun updatePositionInDb(position:SleepPosition) {
    database.sleepPositionDao.insert(position)
  }
}
