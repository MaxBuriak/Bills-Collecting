package com.dramtar.billscollecting.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Dramtar on 16.08.2022
 */
private const val TIME_STAMP_FORMAT = "EEEE, MMMM d, yyyy - hh:mm:ss a"
private const val DATE_YEAR_MONTH_DAY_FORMAT = "yyyy/MM/dd"
private const val DATE_DAY_MONTH_YEAR_FORMAT = "dd/MM/yyyy"
private const val DATE_DAY_DAY_OF_WEEK_FORMAT = "EEE dd MMM"
private const val DATE_HOUR_MINUTE_FORMAT = "HH:mm"
private const val DATE_MONTH_YEAR_FORMAT = "MMM yyyy"

fun Long.getTimeStamp(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getYearMonthDay(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_YEAR_MONTH_DAY_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getMonthYear(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_MONTH_YEAR_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getDayMonthYear(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_DAY_MONTH_YEAR_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getDayDayOfWeek(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_DAY_DAY_OF_WEEK_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getHourMinute(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_HOUR_MINUTE_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

@Throws(ParseException::class)
fun String.getDateUnixTime(dateFormat: String): Long {
    try {
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.parse(this)!!.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    throw ParseException("Please Enter a valid date", 0)
}