package com.dramtar.billscollecting.presenter.utils

import java.util.*

object Utils {
    fun getMinMaxDate(selectedDateRange: Date, calender: Calendar): MinMaxDateInMilli {
        calender.time = selectedDateRange
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH))
        calender.set(Calendar.HOUR_OF_DAY, 0)
        val min = calender.timeInMillis
        calender.set(Calendar.HOUR_OF_DAY, 0)
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH))
        val max = calender.timeInMillis
        return MinMaxDateInMilli(min = min, max = max)
    }

    data class MinMaxDateInMilli(
        val min: Long,
        val max: Long
    )
}
