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
import androidx.core.content.ContextCompat
import kotlin.math.abs


class SensorActivity : AppCompatActivity(), SensorEventListener {


  private lateinit var sensorManager: SensorManager

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sensor)
    Log.i("SensorActivity", "onCreate Called yipee")

    val model: SensorViewModel by viewModels()
    model.startLogWorker()

  }

  override fun onResume() {
    super.onResume()

  }

  override fun onPause() {
    super.onPause()

  }



  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

  }

  override fun onSensorChanged(event: SensorEvent) {


  }




}
