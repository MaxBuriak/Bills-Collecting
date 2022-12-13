package com.dramtar.billscollecting.utils

import java.text.NumberFormat
import kotlin.math.roundToInt

fun Double.fmtLocalCurrency(): String = NumberFormat.getCurrencyInstance().format(this)
fun Float.fmtPercentage(): String = "${(this * 100).roundToInt()}%"
fun Int.fmtLocalCurrency(): String = NumberFormat.getCurrencyInstance().format(this)
fun Double.getFMTLocalCur(): String = NumberFormat.getCurrencyInstance().format(this)
