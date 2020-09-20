package com.example.moonlight.individual

import android.R
import android.os.Bundle
import com.example.moonlight.convertLongToTimeString
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.databinding.FragmentIndividualBinding
import com.example.moonlight.sensor.staticClockDataStructure
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate



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
  var chart: ScatterChart = binding.chart
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

  var dataSet = ScatterDataSet(entries, "Sleep")
  val scatterData = ScatterData(dataSet)
  chart.data = scatterData
  chart.data.isHighlightEnabled = false
}
