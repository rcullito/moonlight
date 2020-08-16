package com.example.moonlight

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.moonlight.database.SleepPosition
import java.text.SimpleDateFormat
import kotlin.math.abs

val zero = 0.0
val sideBack = 0.86
val side = 1.37
val sideStomach = 2.2
val stomach = 3.14

fun convertNumericQualityToString(roll: Float, pitch: Float): String {

  val pitch_absolute_double = abs(pitch.toDouble())

  if (pitch_absolute_double > 0.45) {
    return "upright"
  }

  if (roll > 0.0) {
    return when (roll) {
      in 0.0..0.70 -> "left back"
      in 0.70..1.37 -> "left side back"
      in 1.37..2.2 -> "left side stomach"
      in 2.2..3.15 -> "left stomach"
      else -> "toast"
    }
  } else {
    val roll_absolute_double = abs(roll.toDouble())
    return when (roll_absolute_double) {
      in 0.0..0.70 -> "right back"
      in 0.70..1.37 -> "right side back"
      in 1.37..2.2 -> "right side stomach"
      in 2.2..3.15 -> "right stomach"
      else -> "toast"
    }
  }


}

fun convertLongToDateString(systemTime: Long): String {
  // return SimpleDateFormat("EEEE MMM-dd-yyyy' 'HH:mm")
  return SimpleDateFormat("MMM-dd' 'HH:mm")
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
