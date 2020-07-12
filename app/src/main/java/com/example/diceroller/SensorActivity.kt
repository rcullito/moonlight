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


  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.i("SensorActivity", "onCreate Called yipee")

  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    TODO("Not yet implemented")
  }

  override fun onSensorChanged(event: SensorEvent?) {
    TODO("Not yet implemented")
  }

}
