package com.dramtar.billscollecting.presenter

import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import java.util.*

data class BillsState(
    val bills: List<BillData>? = null,
    val billTypes: List<BillTypeData> = listOf(),
    val tmpBillType: BillTypeData? = null,
    val totalSum: Double = 0.0,
    val formattedTotalSum: String = "",
    val isLoading: Boolean = false,
    val selectedBillType: BillTypeData = BillTypeData(),
    val errorString: String? = null,
    val selectedDateRange: Date = Calendar.getInstance().time,
    val gropedByDateBills: Map<String, List<BillData>>? = null,
    val overviewData: OverviewData? = null,
    val typeOverviewData: TypeOverviewData = TypeOverviewData()
)