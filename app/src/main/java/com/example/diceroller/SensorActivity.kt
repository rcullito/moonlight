package com.example.diceroller

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class SensorActivity : AppCompatActivity(), SensorEventListener {

  private lateinit var sensorManager: SensorManager
  private var sensor: Sensor? = null


  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.i("SensorActivity", "onCreate Called yipee")

    val model: SensorViewModel by viewModels()
    model.startLogWorker()


  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    TODO("Not yet implemented")
  }

  override fun onSensorChanged(event: SensorEvent?) {
    TODO("Not yet implemented")
  }


}
