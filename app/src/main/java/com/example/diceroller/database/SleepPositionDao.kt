package com.example.diceroller.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.sql.Date

@Dao
interface SleepPositionDao {

  @Insert
  fun insert(position: SleepPosition)

  @Query("SELECT * FROM sleep_position_table ORDER BY sleepPositionId DESC")
  fun getAllPositions(): LiveData<List<SleepPosition>>

  @Query("SELECT * FROM sleep_position_table ORDER BY sleepPositionId DESC LIMIT 1")
  fun getMostRecentPosition(): SleepPosition?

  @Query("SELECT DISTINCT date(round(sleep_position_timestamp / 1000), 'unixepoch')  FROM sleep_position_table")
  fun getUniqueDates(): LiveData<List<String>>

}
