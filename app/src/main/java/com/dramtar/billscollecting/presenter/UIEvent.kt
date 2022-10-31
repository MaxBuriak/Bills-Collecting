package com.dramtar.billscollecting.presenter

import java.io.File
import java.util.*

sealed class UIEvent {
    data class SelectDateRange(val date: Date): UIEvent()
    data class ExportToCSV(val file: File): UIEvent()
}
