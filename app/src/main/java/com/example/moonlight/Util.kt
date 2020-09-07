package com.example.moonlight

import android.os.Build
import android.os.SystemClock
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import com.example.moonlight.database.SleepPosition
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun checkNotZero(angle: Double): Boolean {
  return angle != 0.0
}

fun decideInRangeRoll(position: Double): Boolean {
  // TODO this fn would need an absolute value for position here
  return position < rollLowerRotationBound || position > rollUpperRotationBound
}

fun decideInRangePitch(pitch: Double, roll: Double): Boolean {

  if (onBackAccordingToRoll(roll)) {
    return false
  }

  return abs(pitch) < guessPitchBackBound || abs(pitch) > guessPitchBackBound
}


fun pos(angle: Double): Boolean {
  return angle >= 0.0
}

fun onBackAccordingToRoll(roll: Double): Boolean {
  val roll_absolute = abs(roll)
  val diffPi = 3.14 - roll_absolute
  val diffZero = roll_absolute

  return diffPi > diffZero
}

fun convertNumericQualityToStringRoll(roll: Double, pitch: Double): String {

  val pitch_absolute = abs(pitch)
  val roll_absolute = abs(roll)

  if (pitch_absolute > upRightAccordingToPitch) {
    return "upright"
  }

  val direction = if(pos(roll)) "left" else "right"

  val positionCategory =  when (roll_absolute) {
    in rollBack -> "back"
    in rollSideBack -> "side back"
    in rollSideStomach -> "side stomach"
    in rollStomach -> "stomach"
    else -> "toast"
  }

  return direction + " " + positionCategory

}

fun convertNumericQualityToStringPitch(roll: Double, pitch: Double): String {

  val roll_absolute = abs(roll)

  if (roll_absolute in 1.00..2.00) {
    return "upright"
  }

  val side = if(pos(pitch)) "left" else "right"
  val facing = if(onBackAccordingToRoll(roll)) "back" else "stomach"

  val pitchPosition = side + " " + facing
  Log.i("Util", pitchPosition)
  return pitchPosition
}

fun deriveEventClockTime(eventTimeStamp: Long): Long {
  // first get an idea of time relative to system boot
  var elapsedRealTimeMillis = SystemClock.elapsedRealtime()
  var eventElapsedRealTimeMillis = TimeUnit.NANOSECONDS.toMillis(eventTimeStamp)
  var timeSinceEventMillis = elapsedRealTimeMillis - eventElapsedRealTimeMillis
  // what time is it actually from a wall clock's perspective
  var currentClockTime = System.currentTimeMillis()
  // when did our event happen in a wall clock sense
  var eventClockTime = currentClockTime - timeSinceEventMillis
  return eventClockTime
}


fun convertLongToDateString(systemTime: Long): String {
  // return SimpleDateFormat("EEEE MMM-dd-yyyy' 'HH:mm")
  return SimpleDateFormat("MMM-dd' 'HH:mm:ss")
    .format(systemTime).toString()
}

fun formatPosition(position: SleepPosition): Spanned {
  val sb = java.lang.StringBuilder()
  sb.apply {
    append("<b>Time:</b>")
    append("\t${convertLongToDateString(position.sleepPositionTime)}")
    append(" ")
    //  append("<b>Pitch: </b>")
    //  append("%.2f".format(it.pitch))
    // append(" ")
    append("<b>Roll: </b>")
    append("%.2f".format(position.roll))
    append(" ")
    append("<b>Position: <b>")
    // append("\t${convertNumericQualityToStringRoll(position.roll, position.pitch)}<br>")
    append("\t${convertNumericQualityToStringPitch(position.roll, position.pitch)}<br>")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
      return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

  }
}
