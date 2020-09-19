package com.example.moonlight.sensor

import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.moonlight.*
import com.example.moonlight.database.SleepPosition
import kotlin.math.abs
import kotlin.math.roundToInt

val rotationMatrix = FloatArray(9)
val orientationAngles = FloatArray(3)

class FloatRange(override val start: Float, override val endInclusive: Float) : ClosedRange<Float>

fun robRange(range1: FloatRange, range2: IntRange, value: Float): Int {
  if (value !in range1) throw IllegalArgumentException("value is not within the first range")
  if (range1.endInclusive == range1.start) throw IllegalArgumentException("first range cannot be single-valued")
  return range2.start + ((value - range1.start) * (range2.endInclusive - range2.start) / (range1.endInclusive - range1.start)).toInt()
}

fun onBackAccordingToRoll(roll: Double): Boolean {
  val roll_absolute = abs(roll)
  val diffPi = 3.14 - roll_absolute
  val diffZero = roll_absolute

  return diffPi > diffZero
}

fun decideInRangePitch(pitch: Double, roll: Double): Boolean {

  if (abs(roll) in rollUprightRange && abs(pitch) in pitchRangeWhileUpright) {
    return false
  }

  if (onBackAccordingToRoll(roll)) {
    return true
  } else {
    return abs(pitch) < pitchStomachBound || abs(pitch) > pitchBackBound
  }
}

fun buildMinuteRange(hoursList: List<Int>): MutableList<String> {
  var minutesHours = mutableListOf<String>()
  for (hour in hoursList) for (minute in 0..59)
    minutesHours.add("$hour:$minute")

  return minutesHours
}

val top_hours = listOf(9, 10, 11, 12, 1, 2)
val bottom_hours = listOf(3, 4, 5, 6, 7, 8)

val top_of_clock_range = buildMinuteRange(top_hours)
val bottom_of_clock_range = buildMinuteRange(bottom_hours)

val top_of_clock_start_index = 0
val top_of_clock_end_index = top_of_clock_range.count() - 1
val clockIntRange = IntRange(top_of_clock_start_index, top_of_clock_end_index)

fun cooCoo(pitch: Double, roll: Double): String {

  var floatPitch = pitch.toFloat()

  var sixHourIndex =  robRange(FloatRange(-1.57f, 1.57f), clockIntRange, floatPitch)

  if (onBackAccordingToRoll(roll)) {

    return top_of_clock_range[sixHourIndex]

  } else {

    return bottom_of_clock_range[sixHourIndex]
  }
}

@RequiresApi(Build.VERSION_CODES.O)
fun updateOrientationAngles(accelerometerReading: FloatArray, magnetometerReading: FloatArray, eventTimestamp: Long, ctx: Context): SleepPosition {
  SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
  SensorManager.getOrientation(rotationMatrix, orientationAngles)

  var pitch = orientationAngles.get(1).toDouble()
  var roll = orientationAngles.get(2).toDouble()

  if (decideInRangePitch(pitch, roll)) {
    motionVibrate(ctx)
  }

  var wallClock = cooCoo(pitch, roll)

  return SleepPosition(pitch = pitch, roll = roll, sleepPositionTime = eventTimestamp, wallClock = wallClock)
}
