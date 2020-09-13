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

fun decideInRangeRoll(position: Double): Boolean {
  // TODO this fn would need an absolute value for position here
  return position < rollLowerRotationBound || position > rollUpperRotationBound
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

@RequiresApi(Build.VERSION_CODES.O)
fun updateOrientationAngles(accelerometerReading: FloatArray, magnetometerReading: FloatArray, eventTimestamp: Long, ctx: Context): SleepPosition {
  SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
  SensorManager.getOrientation(rotationMatrix, orientationAngles)

  var pitch = orientationAngles.get(1).toDouble()
  var roll = orientationAngles.get(2).toDouble()

  if (decideInRangePitch(pitch, roll)) {
    motionVibrate(ctx)
  }

  return SleepPosition(pitch = pitch, roll = roll, sleepPositionTime = eventTimestamp)
}
