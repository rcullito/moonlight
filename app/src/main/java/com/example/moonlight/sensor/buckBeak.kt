package com.example.moonlight.sensor

fun buildMinuteRange(hoursList: List<Int>): MutableList<String> {
  var minutesHours = mutableListOf<String>()
  for (hour in hoursList) for (minute in 0..59)
    minutesHours.add("$hour:$minute")

  return minutesHours
}

val top_hours = listOf(9, 10, 11, 12, 1, 2)
val bottom_hours = listOf(3, 4, 5, 6, 7, 8)

val top_of_clock_range = buildMinuteRange(top_hours)
val bottom_of_clock_range = buildMinuteRange(bottom_hours)

val top_of_clock_start_index = 0
val top_of_clock_end_index = top_of_clock_range.count() - 1
val clockIntRange = IntRange(top_of_clock_start_index, top_of_clock_end_index)


fun buildHalfOfClockInts(range1: IntRange, range2: IntRange): MutableList<Int> {
  var topOfClockInts = mutableListOf<Int>()
  for (i in (range1)) topOfClockInts.add(i)
  for (i in (range2)) topOfClockInts.add(i)
  return topOfClockInts
}

var topHalfInts = buildHalfOfClockInts(540..719, 0..179)
var bottomHalfInts = buildHalfOfClockInts(180..359, 360..539)




