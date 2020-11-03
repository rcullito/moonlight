package com.example.moonlight.sensor

import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.moonlight.*
import com.example.moonlight.database.SleepPosition
import kotlin.math.abs
import kotlin.math.roundToInt

val rotationMatrix = FloatArray(9)
val orientationAngles = FloatArray(3)

class FloatRange(override val start: Float, override val endInclusive: Float) : ClosedRange<Float>

val rollRange = FloatRange(-3.14f, 3.14f)

fun floatToIntRange(range1: FloatRange, range2: IntRange, value: Float): Int {
  return range2.start + ((value - range1.start) * (range2.endInclusive - range2.start) / (range1.endInclusive - range1.start)).toInt()
}


// this is what we actually want to use
fun onBackAccordingToPitch(pitch: Double): Boolean {
  val pitch_absolute = abs(pitch)

  return pitch_absolute > upRightAccordingToPitch
}

fun cooCoo(roll: Double): Int {
  var tempindex = floatToIntRange(rollRange, 0..719, roll.toFloat())
  return rollInts[tempindex]
}

@RequiresApi(Build.VERSION_CODES.O)
fun updateOrientationAngles(accelerometerReading: FloatArray, magnetometerReading: FloatArray, eventTimestamp: Long, ctx: Context): SleepPosition {
  SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
  SensorManager.getOrientation(rotationMatrix, orientationAngles)

  var pitch = orientationAngles.get(1).toDouble()
  var roll = orientationAngles.get(2).toDouble()

  if (interfere) {
    motionVibrate(ctx)
  }

  var wallClock = cooCoo(roll)

  if (logAngles) {
    Log.i("Angles", pitch.toString())
    Log.i("Angles", roll.toString())
    Log.i("Angles", wallClock.toString())
  }


  return SleepPosition(pitch = pitch, roll = roll, sleepPositionTime = eventTimestamp, wallClock = wallClock)
}
