package com.example.diceroller

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class SensorActivity : AppCompatActivity() {


  private lateinit var model: SensorViewModel

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sensor)
    Log.i("SensorActivity", "onCreate Called yipee")

    val model: SensorViewModel by viewModels()

    model.startLogWorker()

  }

  fun cancelWork(view: View) {
    // TODO just see if this can do something before we worry about "the work"
    Log.i("SensorActivity", "cancelWork function called")
  }
}
