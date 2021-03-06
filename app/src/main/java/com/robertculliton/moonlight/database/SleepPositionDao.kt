package com.robertculliton.moonlight.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SleepPositionDao {

  @Insert
  fun insert(position: SleepPosition)

  @Query("SELECT * FROM sleep_position_table ORDER BY sleepPositionId DESC")
  fun getAllPositions(): LiveData<List<SleepPosition>>

  @Query("SELECT * FROM sleep_position_table ORDER BY sleepPositionId DESC LIMIT 1")
  fun getMostRecentPosition(): SleepPosition?

  @Query("SELECT DISTINCT date(round(sleep_position_timestamp / 1000), 'unixepoch') AS date FROM sleep_position_table ORDER BY sleep_position_timestamp DESC")
  fun getUniqueDates(): LiveData<List<SleepDate>>

  @Query("SELECT * FROM sleep_position_table WHERE date(round(sleep_position_timestamp / 1000), 'unixepoch') BETWEEN :beforeDate AND :afterDate")
  fun getSpecificDate(beforeDate: String, afterDate: String): LiveData<List<SleepPosition>>

}
