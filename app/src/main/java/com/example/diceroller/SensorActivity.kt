package com.example.diceroller

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class SensorActivity : AppCompatActivity(){


  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.i("SensorActivity", "onCreate Called yipee")

    val model: SensorViewModel by viewModels()
    model.startLogWorker()


  }


}
