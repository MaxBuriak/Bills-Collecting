package com.dramtar.billscollecting.presenter.utils.composedatepicker

import java.util.*

interface SelectDateListener {
    fun onDateSelected(date: Date)
    fun onCanceled()
}