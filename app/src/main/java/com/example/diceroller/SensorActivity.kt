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
import kotlin.math.abs


class SensorActivity : AppCompatActivity(), SensorEventListener {

  private lateinit var sensorManager: SensorManager
  private val accelerometerReading = FloatArray(3)
  private val magnetometerReading = FloatArray(3)
  val rotationMatrix = FloatArray(9)
  val orientationAngles = FloatArray(3)
  private val VALUE_DRIFT: Float = 0.1F;

  private lateinit var textSensorAzimuth: TextView
  private lateinit var textSensorPitch: TextView
  private lateinit var textSensorRoll: TextView


  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sensor)
    Log.i("SensorActivity", "onCreate Called yipee")

    textSensorAzimuth = findViewById<TextView>(R.id.value_azimuth)
    textSensorPitch = findViewById<TextView>(R.id.value_pitch)
    textSensorRoll = findViewById<TextView>(R.id.value_roll)

    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // val model: SensorViewModel by viewModels()
    // model.startLogWorker()


  }

  override fun onResume() {
    super.onResume()
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
  }

  override fun onPause() {
    super.onPause()
    sensorManager.unregisterListener(this)
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

    if (abs(pitch) < VALUE_DRIFT) {
      pitch = 0.0F
    }
    if (abs(roll) < VALUE_DRIFT) {
      roll = 0.0F
    }

    textSensorAzimuth.setText(azimuth.toString())
    textSensorPitch.setText(pitch.toString())
    textSensorRoll.setText(roll.toString())
  }


}
