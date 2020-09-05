package com.example.moonlight.sensor

import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.lowerRotationBound
import com.example.moonlight.upperRotationBound
import kotlin.math.abs

val rotationMatrix = FloatArray(9)
val orientationAngles = FloatArray(3)
private var lastUpdate: Long = 0

fun checkZero(angle: Float): Boolean {
  return angle.toDouble() != 0.0
}

@RequiresApi(Build.VERSION_CODES.O)
fun updateOrientationAngles(accelerometerReading: FloatArray, magnetometerReading: FloatArray, eventTimestamp: Long, ctx: Context): SleepPosition {
  SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
  SensorManager.getOrientation(rotationMatrix, orientationAngles)

  var pitch = orientationAngles.get(1)
  var roll = orientationAngles.get(2)

  if (abs(roll.toDouble()) < lowerRotationBound || abs(roll.toDouble()) > upperRotationBound) {
    motionVibrate(ctx)
  }

  if (checkZero(pitch) && checkZero(roll)) {
    var currentEventTime = eventTimestamp

    if ((currentEventTime - lastUpdate) > (1000)) {


      Log.i("SensorWorker/pitch", pitch.toString())
      Log.i("SensorWorker/roll", roll.toString())

      lastUpdate = currentEventTime

    }
  }

  return SleepPosition(pitch = pitch, roll = roll, sleepPositionTime = eventTimestamp)
}
