package com.example.moonlight.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_position_table")
data class SleepPosition(

  @PrimaryKey(autoGenerate = true)
  var sleepPositionId: Long = 0L,

  @ColumnInfo(name = "sleep_position_timestamp")
  val sleepPositionTime: Long,

  @ColumnInfo(name = "pitch")
  var pitch: Double,

  @ColumnInfo(name = "roll")
  var roll: Double,

  @ColumnInfo(name = "wall_clock")
  var wallClock:  Int

)
