package com.example.diceroller

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class SensorActivity : AppCompatActivity(), SensorEventListener {

  private lateinit var sensorManager: SensorManager
  private val accelerometerReading = FloatArray(3)
  private val magnetometerReading = FloatArray(3)
  val rotationMatrix = FloatArray(9)
  val orientationAngles = FloatArray(3)


  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sensor)
    Log.i("SensorActivity", "onCreate Called yipee")

    val textSensorAzimuth = findViewById<TextView>(R.id.value_azimuth)
    val textSensorPitch = findViewById<TextView>(R.id.value_pitch)
    val textSensorRoll = findViewById<TextView>(R.id.value_roll)

    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val model: SensorViewModel by viewModels()
    model.startLogWorker()


  }

  override fun onResume() {
    super.onResume()
    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
      sensorManager.registerListener(
        this,
        accelerometer,
        // TODO think more about delays here
        SensorManager.SENSOR_DELAY_NORMAL,
        SensorManager.SENSOR_DELAY_UI
      )
    }
    sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
      sensorManager.registerListener(
        this,
        magneticField,
        // TODO think more about delays here
        SensorManager.SENSOR_DELAY_NORMAL,
        SensorManager.SENSOR_DELAY_UI
      )
    }
  }

  override fun onPause() {
    super.onPause()
    sensorManager.unregisterListener(this)
  }



  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    TODO("Not yet implemented")
  }

  override fun onSensorChanged(event: SensorEvent) {

    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
      System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
    } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
      System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
    }

  }

  fun updateOrientationAngles() {
    SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
    SensorManager.getOrientation(rotationMatrix, orientationAngles)
    val azimuth = orientationAngles.get(0)
    val pitch = orientationAngles.get(1)
    val roll = orientationAngles.get(2)
  }


}
