package com.example.moonlight.individual

import com.example.moonlight.database.SleepPosition
import com.example.moonlight.databinding.FragmentIndividualBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

fun buildChart(binding: FragmentIndividualBinding, sleepPositions: List<SleepPosition>) {
  var chart: LineChart = binding.chart as LineChart
  chart.isScaleYEnabled = false

  chart.axisRight.isEnabled = false
  val xAxis = chart.xAxis

  xAxis.valueFormatter = MyXAxisFormatter()
  xAxis.setDrawGridLines(false)

  val leftAxis = chart.axisLeft
  leftAxis.setDrawGridLines(false)

  var entries: ArrayList<Entry> = ArrayList()

  for (position in sleepPositions) {
    entries.add(Entry(position.sleepPositionTime.toFloat(), position.pitch.toFloat()));
  }

  var dataSet = LineDataSet(entries, "Sleep")
  val lineData = LineData(dataSet)
  chart.data = lineData
  chart.data.isHighlightEnabled = false
}
