package com.io.eatdecider.database

import com.io.eatdecider.database.dao.HistoryDatabaseDao
import com.io.eatdecider.database.entity.HistoryItem
import com.io.eatdecider.models.Place
import com.io.eatdecider.models.PreviousPlace
import com.io.eatdecider.models.format
import java.time.LocalDate

class Repository(private val databaseDao: HistoryDatabaseDao) {

    suspend fun getHistory(): MutableList<PreviousPlace> {
        return try {
            val history: MutableList<PreviousPlace> = databaseDao.getAll().map {
                PreviousPlace(
                    imageId = it.imageId,
                    placeName = it.placeName,
                    dayOfWeek = it.dayOfWeek
                )
            }.reversed() as MutableList<PreviousPlace>

            history
        } catch (exception: Exception) {
            mutableListOf()
        }
    }

    suspend fun addToHistory(placePicked: Place) {
        databaseDao.addToHistory(
            HistoryItem(
                placeName = placePicked.placeName,
                imageId = placePicked.imageId,
                dayOfWeek = LocalDate.now().dayOfWeek.format()
            )
        )
    }

    suspend fun deleteAll() {
        databaseDao.deleteAll()
    }

}