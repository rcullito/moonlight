package com.example.moonlight.sensor

import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.moonlight.*
import com.example.moonlight.database.SleepPosition
import kotlin.math.abs

val rotationMatrix = FloatArray(9)
val orientationAngles = FloatArray(3)

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

// extension function assumed to always act on our time range
fun ClosedFloatingPointRange<Double>.convert(number: Double, target: ClosedFloatingPointRange<Double>): Double {
  val ratio = number.toFloat() / (endInclusive - start)
  return ratio * (target.endInclusive - target.start)
}

// welp ok, nice, we are halfway through both, but we need our target range to be in minutes!

fun buildMinuteRange(): MutableList<String> {
  var minutesHours = mutableListOf<String>()
  for (minute in 0..60) for (hour in 0..3)
    minutesHours.add(hour.toString() + minute.toString())

  return minutesHours
}



val result =  (-0.0..-1.57).convert(-0.78, 0.0..3.0)


fun cooCoo(pitch: Double, roll: Double): Double {
  // if we are on our back according to roll, then our range is JOIN 9-12, 0-3

  // pitch of 0 is 12 noon
  //  -1.57 is 3
  // -0.78 is 1:30
  // 0.78 is 10:30
  // 1.57 is 9


  // if we are on our stomach according to roll, then our range is 3-9

  // then it becomes a mapping of ranges
  // then if want to get fancy, going from base 100 to base 60 for the minutes
  // round to nearest minute

  return 12.57
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
