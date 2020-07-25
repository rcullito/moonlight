package com.example.diceroller

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.diceroller.database.SleepPosition
import java.text.SimpleDateFormat

// TODO mapping function with when to convert radians to  "directly on side, side towards stomach, stomach"

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
      append("<b>Pitch: </b>")
      append("%.2f".format(it.pitch))
      append(" ")
      append("<b>Roll: </b>")
      append("%.2f".format(it.roll))
      append("<br>")
    }
  }

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
  } else {
    return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
  }
}
