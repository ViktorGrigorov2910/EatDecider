package com.io.eatdecider.database.dao

import androidx.room.*
import com.io.eatdecider.database.entity.HistoryItem

@Dao
interface HistoryDatabaseDao {

    @Query("SELECT * from history_list")
    suspend fun getAll(): MutableList<HistoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToHistory(historyItem: HistoryItem)

    @Query("DELETE FROM history_list")
    suspend fun deleteAll()

}