package com.example.moonlight.individual

import com.example.moonlight.convertLongToTimeString
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.databinding.FragmentIndividualBinding
import com.example.moonlight.sensor.staticClockDataStructure
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

class MyXAxisFormatter : ValueFormatter() {
  override fun getAxisLabel(value: Float, axis: AxisBase?): String {
    return convertLongToTimeString(value.toLong(), "HH:mm:ss")
  }
}

class MyYAxisFormatter : ValueFormatter() {
  override fun getAxisLabel(value: Float, axis: AxisBase?): String {
    return staticClockDataStructure[value.toInt()]
  }
}

fun buildChart(binding: FragmentIndividualBinding, sleepPositions: List<SleepPosition>) {
  var chart: LineChart = binding.chart as LineChart
  chart.isScaleYEnabled = false

  chart.axisRight.isEnabled = false
  val xAxis = chart.xAxis

  xAxis.valueFormatter = MyXAxisFormatter()
  xAxis.setDrawGridLines(false)

  val leftAxis = chart.axisLeft
  leftAxis.valueFormatter = MyYAxisFormatter()
  leftAxis.setDrawGridLines(false)

  var entries: ArrayList<Entry> = ArrayList()

  for (position in sleepPositions) {
    entries.add(Entry(position.sleepPositionTime.toFloat(), position.wallClock.toFloat()));
  }

  var dataSet = LineDataSet(entries, "Sleep")
  val lineData = LineData(dataSet)
  chart.data = lineData
  chart.data.isHighlightEnabled = false
}
