package com.dramtar.billscollecting.presenter

import com.dramtar.billscollecting.domain.BillTypeData
import java.io.File
import java.util.*

sealed class UIEvent {
    data class SelectDateRange(val date: Date): UIEvent()
    data class ExportToCSV(val file: File): UIEvent()
    data class ShowTypeOverview(val type: BillTypeData): UIEvent()
    object ShowCurrentMonthBillsOverviewData : UIEvent()
    object ShowAllBillsOverviewData : UIEvent()
}
