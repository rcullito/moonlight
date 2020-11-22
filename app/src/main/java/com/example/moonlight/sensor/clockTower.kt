package com.example.moonlight.sensor


fun buildCleanMinutes(minutesList: IntRange): MutableList<String> {
  var cleanStrings = mutableListOf<String>()

  for (rawMinute in minutesList) {
    var cleanMinute = if (rawMinute < 10) "0".plus(rawMinute.toString()) else rawMinute.toString()
    cleanStrings.add(cleanMinute)
  }

  return cleanStrings
}

var cleanMinutes: MutableList<String> = buildCleanMinutes(0..59)

fun buildMinuteRange(hoursList: List<Int>, cleanMinutesList: List<String>): MutableList<String> {
  var minutesHours = mutableListOf<String>()
  for (hour in hoursList) for (cleanMinute in cleanMinutesList)
  minutesHours.add("$hour:$cleanMinute")

  minutesHours.removeAt(1) // because no one will notice if 12:01 is missing
  minutesHours.add("12:00")

  return minutesHours
}

var hours = listOf(12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)

var staticClockDataStructure: MutableList<String> = buildMinuteRange(hours, cleanMinutes)


fun buildHalfOfClockInts(range1: IntRange, range2: IntRange): MutableList<Int> {
  var topOfClockInts = mutableListOf<Int>()
  for (i in (range1)) topOfClockInts.add(i)
  for (i in (range2)) topOfClockInts.add(i)
  return topOfClockInts
}

var rollInts = buildHalfOfClockInts( 360..719, 0..359)





