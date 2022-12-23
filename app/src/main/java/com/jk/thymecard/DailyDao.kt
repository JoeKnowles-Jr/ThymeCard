package com.jk.thymecard

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DailyDao {

    @Query("SELECT * FROM dailies ORDER BY createdAt ASC")
    fun getDailies(): LiveData<List<Daily>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(daily: Daily)

    @Delete
    fun delete(daily: Daily)

}