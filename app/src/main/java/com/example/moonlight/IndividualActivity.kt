package com.example.moonlight

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart

class IndividualActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_individual)
    val chart: LineChart = findViewById(R.id.chart) as LineChart
  }
}
