package com.dramtar.billscollecting.utils

import java.text.NumberFormat
import kotlin.math.roundToInt

fun Double.getFMTLocalCurrency(): String = NumberFormat.getCurrencyInstance().format(this)
fun Float.getFormattedPercentage(): String = "${(this * 100).roundToInt()}%"
fun Int.getFMTLocalCurrency(): String = NumberFormat.getCurrencyInstance().format(this)
fun Double.getFtdLocalCur(): String = NumberFormat.getCurrencyInstance().format(this)
