package com.dramtar.billscollecting.presenter

import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.BillTypeGrouped
import java.util.*

data class BillsState(
    val bills: List<BillData>? = null,
    val billTypes: List<BillTypeData> = listOf(),
    val tmpBillType: BillTypeData? = null,
    val totalSum: Int = 0,
    val formattedTotalSum: String = "",
    val isLoading: Boolean = false,
    val selectedBillTypeId: String = "",
    var errorString: String? = null,
    var selectedDateRange: Date = Calendar.getInstance().time,
    var overviewTypesList: List<BillTypeGrouped>? = null,
    var gropedByDateBillsList: Map<String, List<BillData>>? = null
)