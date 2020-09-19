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
// get the count of this, and then the ratio will give us the index
val top_of_clock_range = buildMinuteRange(top_hours)
val top_of_clock_start_index = 0
val top_of_clock_end_index = top_of_clock_range.count() - 1
val clockIntRange = IntRange(top_of_clock_start_index, top_of_clock_end_index)




fun cooCoo(pitch: Double, roll: Double): String {
  // if we are on our back according to roll, then our range is  9-12, 0-3

  var floatPitch = pitch.toFloat()
  if (onBackAccordingToRoll(roll)) {
    var sixHourIndex =  robRange(FloatRange(-1.57f, 1.57f), clockIntRange, floatPitch)
    var pitchClockTime = top_of_clock_range[sixHourIndex]
    return pitchClockTime
  }

  // pitch of 0 is 12 noon
  //  -1.57 is 3
  // -0.78 is 1:30
  // 0.78 is 10:30
  // 1.57 is 9


  // if we are on our stomach according to roll, then our range is 3-9

  // then it becomes a mapping of ranges
  // then if want to get fancy, going from base 100 to base 60 for the minutes
  // round to nearest minute

  return "up next is the other six hours"
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

  return SleepPosition(pitch = pitch, roll = roll, sleepPositionTime = eventTimestamp, wallClock = 12.57)
}
