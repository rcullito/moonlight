package com.example.diceroller.database

data class SleepPosition(
  var sleepPositionId: Long = 0L,
  val sleepPositionTime: Long = System.currentTimeMillis(),
  var pitch: Double = 0.0,
  var roll: Double = 0.0

)
