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
import kotlin.math.abs

class SensorWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params), SensorEventListener {

  private lateinit var sensorManager: SensorManager
  private val accelerometerReading = FloatArray(3)
  private val magnetometerReading = FloatArray(3)
  val rotationMatrix = FloatArray(9)
  val orientationAngles = FloatArray(3)

  override suspend fun doWork(): Result {

    sensorManager = this.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
      sensorManager.registerListener(
        this,
        accelerometer,
        SensorManager.SENSOR_DELAY_FASTEST,
        SensorManager.SENSOR_DELAY_FASTEST
      )
    }
    sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
      sensorManager.registerListener(
        this,
        magneticField,
        SensorManager.SENSOR_DELAY_FASTEST,
        SensorManager.SENSOR_DELAY_FASTEST
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
    val azimuth = orientationAngles.get(0)
    var pitch = orientationAngles.get(1)
    var roll = orientationAngles.get(2)

    Log.i("SensorWorker", azimuth.toString())
    Log.i("SensorWorker", pitch.toString())
    Log.i("SensorWorker", roll.toString())

    // cross yourself

    if (azimuth.toDouble() != 0.0) {
      sensorManager.unregisterListener(this)
    }


  }
}
