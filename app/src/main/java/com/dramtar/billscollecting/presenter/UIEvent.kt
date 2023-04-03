package com.dramtar.billscollecting.presenter

import java.util.*

sealed class UIEvent {
    data class SelectDateRange(val date: Date) : UIEvent()
}
