package com.example.moonlight

import java.text.SimpleDateFormat


fun convertLongToTimeString(systemTime: Long, desiredDateFormat: String): String {
  // return SimpleDateFormat("EEEE MMM-dd-yyyy' 'HH:mm")
  return SimpleDateFormat(desiredDateFormat)
    .format(systemTime).toString()
}

