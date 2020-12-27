package com.robertculliton.moonlight.individual

import android.annotation.SuppressLint
import android.graphics.Color
import com.robertculliton.moonlight.database.SleepPosition
import com.robertculliton.moonlight.databinding.FragmentIndividualBinding
import com.robertculliton.moonlight.sensor.staticClockDataStructure
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat


var baseTime: Long = 0

fun convertLongToTimeString(systemTime: Long, desiredDateFormat: String): String {
  // return SimpleDateFormat("EEEE MMM-dd-yyyy' 'HH:mm")
  return SimpleDateFormat(desiredDateFormat)
    .format(systemTime).toString()
}

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

@SuppressLint("ResourceType")
fun buildChart(binding: FragmentIndividualBinding, sleepPositions: List<SleepPosition>) {

  baseTime = sleepPositions.first().sleepPositionTime

  var chart: ScatterChart = binding.chart

  chart.description.isEnabled = false
  chart.isScaleYEnabled = false

  chart.axisRight.isEnabled = false
  val xAxis = chart.xAxis

  xAxis.valueFormatter = MyXAxisFormatter()
  xAxis.setDrawGridLines(false)

  val leftAxis = chart.axisLeft
  leftAxis.valueFormatter = MyYAxisFormatter()
  leftAxis.setDrawGridLines(false)
  leftAxis.axisMaximum = 719F
  leftAxis.axisMinimum = 0F
  leftAxis.setLabelCount(13, true)

  var entries: ArrayList<Entry> = ArrayList()

  for (position in sleepPositions) {

    var xValue = (position.sleepPositionTime - baseTime).toFloat()
    var yValue = position.wallClock.toFloat()

    entries.add(Entry(xValue, yValue));
  }

  var dataSet = ScatterDataSet(entries, "recording sleep angle (left) at a specific time (top)")
  dataSet.setColor(Color.BLUE)
  dataSet.setDrawValues(false)
  val scatterData = ScatterData(dataSet)
  chart.data = scatterData
  chart.data.isHighlightEnabled = false
  dataSet.highLightColor = Color.BLUE;




  chart.invalidate()
}
