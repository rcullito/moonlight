package com.example.diceroller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

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
