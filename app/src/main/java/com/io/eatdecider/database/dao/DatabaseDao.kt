package com.io.eatdecider.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.io.eatdecider.database.entity.HistoryItem

@Database(entities = [HistoryItem::class] , version = 1, exportSchema = false)
abstract class DatabaseDao : RoomDatabase(){

    abstract fun dao(): HistoryDatabaseDao

    companion object {
        private var INSTANCE: DatabaseDao? = null
        fun getInstance(context: Context): DatabaseDao {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseDao::class.java,
                        "history_list")
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}