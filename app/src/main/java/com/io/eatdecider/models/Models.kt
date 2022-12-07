package com.io.eatdecider.models

import com.io.eatdecider.util.*
import java.time.DayOfWeek
import java.time.LocalDate

data class Place(val imageId: Int, var placeName: String) {

    fun toPreviousPlace(): PreviousPlace = PreviousPlace(
        imageId = imageId,
        placeName = placeName,
        dayOfWeek = LocalDate.now().dayOfWeek.format()
    )

}

data class PreviousPlace(val imageId: Int, val placeName: String, val dayOfWeek: String)

fun DayOfWeek.format(): String {
    return when (this.ordinal) {
        0 -> MONDAY
        1 -> TUESDAY
        2 -> WEDNESDAY
        3 -> THURSDAY
        4 -> FRIDAY
        5 -> SATURDAY
        6 -> SUNDAY
        else -> { UNKNOWN }
    }
}