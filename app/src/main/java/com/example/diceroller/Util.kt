package com.example.diceroller

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.diceroller.database.SleepPosition
import java.text.SimpleDateFormat
import kotlin.math.abs

fun convertNumericQualityToString(roll: Float): String {

  val absolute_double = abs(roll.toDouble())

  return when (absolute_double) {
    in 0.0..0.86 -> "left back"
    in 0.86..1.37 -> "left side back"
    in 1.37..2.2 -> "left side stomach"
    in 2.2..3.14 -> "left stomach"
    in 0.0..-0.86 -> "right back"
    in -0.86..-1.37 -> "right side back"
    in -1.37..-2.2 -> "right side stomach"
    in -2.2..-3.14 -> "right stomach"
    else -> "toast"
  }
}

fun convertLongToDateString(systemTime: Long): String {
  // return SimpleDateFormat("EEEE MMM-dd-yyyy' 'HH:mm")
  return SimpleDateFormat("MMM-dd' 'HH:mm")
    .format(systemTime).toString()
}

fun formatPosition(positions: List<SleepPosition>): Spanned {
  val sb = StringBuilder()
  sb.apply {
    append("<h3>Here are your recent recorded positions</h3>")
    positions.forEach {
      append("<br>")
      append("<b>Time:</b>")
      append("\t${convertLongToDateString(it.sleepPositionTime)}")
      append(" ")
      //append("<b>Pitch: </b>")
      //append("%.2f".format(it.pitch))
      //append(" ")
      append("<b>Roll: </b>")
      append("%.2f".format(it.roll))
      append(" ")
      append("<b>Position<b>")
      append("\t${convertNumericQualityToString(it.roll)}<br>")
    }
  }

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
  } else {
    return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
  }
}
