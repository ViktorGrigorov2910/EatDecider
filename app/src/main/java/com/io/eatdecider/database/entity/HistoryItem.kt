package com.io.eatdecider.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_list")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0L,
    @ColumnInfo(name = "place_name")
    val placeName: String,
    @ColumnInfo(name = "day_of_week")
    var dayOfWeek: String,
    @ColumnInfo(name = "image_id")
    var imageId: Int
)