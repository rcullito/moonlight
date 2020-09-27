package com.example.moonlight.individual

import com.example.moonlight.convertLongToTimeString
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.databinding.FragmentIndividualBinding
import com.example.moonlight.sensor.staticClockDataStructure
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.formatter.ValueFormatter


var baseTime: Long = 0

class MyXAxisFormatter : ValueFormatter() {
  override fun getAxisLabel(value: Float, axis: AxisBase?): String {

    var reAddedValue = value.toLong() + baseTime

    return convertLongToTimeString(reAddedValue, "HH:mm:ss")
  }
}

class MyYAxisFormatter : ValueFormatter() {
  override fun getAxisLabel(value: Float, axis: AxisBase?): String {
    return staticClockDataStructure[value.toInt()]
  }
}

fun buildChart(binding: FragmentIndividualBinding, sleepPositions: List<SleepPosition>) {

  baseTime = sleepPositions.first().sleepPositionTime

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

    var xValue = (position.sleepPositionTime - baseTime).toFloat()
    var yValue = position.wallClock.toFloat()

    entries.add(Entry(xValue, yValue));
  }

  var dataSet = ScatterDataSet(entries, "Sleep")
  dataSet.setDrawValues(false)
  val scatterData = ScatterData(dataSet)
  chart.data = scatterData
  chart.data.isHighlightEnabled = false
}
