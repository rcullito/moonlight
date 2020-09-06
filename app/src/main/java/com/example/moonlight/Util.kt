package com.example.moonlight

import android.os.Build
import android.os.SystemClock
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.moonlight.database.SleepPosition
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun convertNumericQualityToString(roll: Double, pitch: Double): String {

  val pitch_absolute = abs(pitch)

  if (pitch_absolute > upRightAccordingToPitch) {
    return "upright"
  }
  // TODO this should be a fun that says right or left depending on the absolute value of roll
  if (roll > 0.0) {
    return when (roll) {
      in rollBack -> "left back"
      in rollSideBack -> "left side back"
      in rollSideStomach -> "left side stomach"
      in rollStomach -> "left stomach"
      else -> "toast"
    }
  } else {
    val roll_absolute_double = abs(roll)
    return when (roll_absolute_double) {
      in rollBack -> "right back"
      in rollSideBack -> "right side back"
      in rollSideStomach -> "right side stomach"
      in rollStomach -> "right stomach"
      else -> "toast"
    }
  }

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
    append("\t${convertNumericQualityToString(position.roll, position.pitch)}<br>")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
      return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

  }
}
