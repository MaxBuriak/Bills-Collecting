package com.dramtar.billscollecting.utils

import java.text.NumberFormat

fun Double.formatCurrency() = NumberFormat.getCurrencyInstance().format(this)