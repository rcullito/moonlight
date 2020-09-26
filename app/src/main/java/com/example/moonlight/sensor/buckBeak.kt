package com.example.moonlight.sensor

fun buildMinuteRange(hoursList: List<Int>): MutableList<String> {
  var minutesHours = mutableListOf<String>()
  for (hour in hoursList) for (minute in 0..59)
    minutesHours.add("$hour:$minute")

  return minutesHours
}

var hours = listOf(12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
// will be used from within the axis formatter
var staticClockDataStructure: MutableList<String> = buildMinuteRange(hours)


fun buildHalfOfClockInts(range1: IntRange, range2: IntRange): MutableList<Int> {
  var topOfClockInts = mutableListOf<Int>()
  for (i in (range1)) topOfClockInts.add(i)
  for (i in (range2)) topOfClockInts.add(i)
  return topOfClockInts
}

var topHalfInts = buildHalfOfClockInts(540..719, 0..179)
var bottomHalfInts = buildHalfOfClockInts(180..359, 360..539)

var rollInts = buildHalfOfClockInts( 360..719, 0..359)





