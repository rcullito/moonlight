package com.example.moonlight.sensor

import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.rollLowerRotationBound
import com.example.moonlight.rollUpperRotationBound
import kotlin.math.abs

val rotationMatrix = FloatArray(9)
val orientationAngles = FloatArray(3)

fun checkNotZero(angle: Double): Boolean {
  return angle != 0.0
}

fun decideInRage(position: Double): Boolean {
  return position < rollLowerRotationBound || position > rollUpperRotationBound
}

@RequiresApi(Build.VERSION_CODES.O)
fun updateOrientationAngles(accelerometerReading: FloatArray, magnetometerReading: FloatArray, eventTimestamp: Long, ctx: Context): SleepPosition {
  SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
  SensorManager.getOrientation(rotationMatrix, orientationAngles)

  var pitch = orientationAngles.get(1).toDouble()
  var roll = orientationAngles.get(2).toDouble()
  var allPositive = listOf<Double>(pitch, roll).all { checkNotZero(it) }

  Log.i("SensorWorker/pitch", pitch.toString())
  Log.i("SensorWorker/roll", roll.toString())

  // TODO this is all about Action.kt
  if (decideInRage(abs(roll))) {
    motionVibrate(ctx)
  }

  // This section is really meant to be about Recording in the Database. So name it as such.
  if (allPositive) {

  }

  return SleepPosition(pitch = pitch, roll = roll, sleepPositionTime = eventTimestamp)
}
