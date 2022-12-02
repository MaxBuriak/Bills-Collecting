package com.dramtar.billscollecting.presenter

import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.BillTypeGrouped
import java.util.*

data class BillsState(
    val bills: List<BillData>? = null,
    val billTypes: List<BillTypeData> = listOf(),
    val tmpBillType: BillTypeData? = null,
    val totalSum: Double = 0.0,
    val formattedTotalSum: String = "",
    val isLoading: Boolean = false,
    val selectedBillTypeId: String = "",
    val errorString: String? = null,
    val selectedDateRange: Date = Calendar.getInstance().time,
    val overviewTypes: List<BillTypeGrouped>? = null,
    val overviewAllBillsTypes: List<BillTypeGrouped>? = null,
    val gropedByDateBillsList: Map<String, List<BillData>>? = null
)