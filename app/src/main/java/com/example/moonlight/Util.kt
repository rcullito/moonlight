package com.example.moonlight

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.sensor.onBackAccordingToRoll
import java.text.SimpleDateFormat
import kotlin.math.abs


fun convertLongToTimeString(systemTime: Long, desiredDateFormat: String): String {
  // return SimpleDateFormat("EEEE MMM-dd-yyyy' 'HH:mm")
  return SimpleDateFormat(desiredDateFormat)
    .format(systemTime).toString()
}

