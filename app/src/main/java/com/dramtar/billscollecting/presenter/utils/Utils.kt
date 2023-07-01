package com.dramtar.billscollecting.presenter.utils

import java.util.*

object Utils {
    fun getMinMaxDate(selectedDateRange: Date, calender: Calendar): MinMaxDateInMilli {
        calender.time = selectedDateRange
        calender.timeZone = TimeZone.getDefault()
        calender.set(Calendar.DAY_OF_MONTH, 1)
        calender.set(Calendar.HOUR_OF_DAY, 0)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.SECOND, 0)
        calender.set(Calendar.MILLISECOND, 0)
        val min = calender.timeInMillis

        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH))
        calender.set(Calendar.HOUR_OF_DAY, 23)
        calender.set(Calendar.MINUTE, 59)
        calender.set(Calendar.SECOND, 59)
        val max = calender.timeInMillis
        return MinMaxDateInMilli(min = min, max = max)
    }

    data class MinMaxDateInMilli(
        val min: Long,
        val max: Long
    )
}
