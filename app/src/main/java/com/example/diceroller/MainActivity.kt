package com.example.diceroller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

  lateinit var diceImage: ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    Log.i("MainActivity", "onCreate Called yipee")
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

  }

  fun startSensor(view: View) {
    val intent = Intent(this, SensorActivity::class.java)
    startActivity(intent)
  }
}
