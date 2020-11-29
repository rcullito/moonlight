  package com.example.moonlight.sensor

import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.res.TypedArrayUtils.getString
import com.example.moonlight.*
import com.example.moonlight.database.SleepPosition
import kotlin.math.abs

val rotationMatrix = FloatArray(9)
val orientationAngles = FloatArray(3)

fun notUpright(pitch: Double): Boolean {
  return abs(pitch) < upRightAccordingToPitch
}


  fun upRight(pitch: Double): Boolean {
    return abs(pitch) > upRightAccordingToPitch
  }
fun onBack(roll: Double): Boolean {
  return abs(roll) < onBackAccordingToRoll
}

class FloatRange(override val start: Float, override val endInclusive: Float) : ClosedRange<Float>

val rollRange = FloatRange(-3.14f, 3.14f)

fun floatToIntRange(range1: FloatRange, range2: IntRange, value: Float): Int {
  return range2.start + ((value - range1.start) * (range2.endInclusive - range2.start) / (range1.endInclusive - range1.start)).toInt()
}

fun rollToSevenTwenty(roll: Double): Int {
  var tempindex = floatToIntRange(rollRange, 0..719, roll.toFloat())
  return rollInts[tempindex]
}

@RequiresApi(Build.VERSION_CODES.O)
fun updateOrientationAngles(accelerometerReading: FloatArray, magnetometerReading: FloatArray, eventTimestamp: Long, ctx: Context): SleepPosition {
  SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
  SensorManager.getOrientation(rotationMatrix, orientationAngles)

  var pitch = orientationAngles.get(1).toDouble()
  var roll = orientationAngles.get(2).toDouble()


  val sharedPref = ctx.applicationContext.getSharedPreferences("lupin", Context.MODE_PRIVATE)
  val interfere = sharedPref.getString("interfere", "none")

  Log.i("ServiceInterfere", interfere)

 if (onBack(roll) && notUpright(pitch)) {

   when (interfere) {
     "vibrate" -> motionVibrate(ctx)
     "chime" -> motionAudio(ctx)
     "both" -> {
       var roulette = (0..1).random()
       if (roulette < 1) motionVibrate(ctx) else motionAudio(ctx)
     }
   }


 }


  var wallClock = rollToSevenTwenty(roll)

  return SleepPosition(pitch = pitch, roll = roll, sleepPositionTime = eventTimestamp, wallClock = wallClock)
}
