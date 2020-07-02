package com.example.diceroller

import android.R
import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log


class SensorActivity : Activity(), SensorEventListener {
  private var mSensorManager: SensorManager? = null
  private var mOrientation: Sensor? = null

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.i("SensorActivity", "onCreate Called yipee")
    mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    mOrientation = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION)
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    // Do something here if sensor accuracy changes.
    // You must implement this callback in your code.
  }

  override fun onResume() {
    super.onResume()
    mSensorManager!!.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL)
  }

  override fun onPause() {
    super.onPause()
    mSensorManager!!.unregisterListener(this)
  }

  override fun onSensorChanged(event: SensorEvent) {
    val azimuth_angle = event.values[0]
    val pitch_angle = event.values[1]
    val roll_angle = event.values[2]
    // Do something with these orientation angles.
    Log.i( "SensorActivity", azimuth_angle.toString())
  }
}
