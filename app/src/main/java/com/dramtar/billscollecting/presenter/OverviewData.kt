package com.dramtar.billscollecting.presenter

import com.dramtar.billscollecting.domain.BillTypeGrouped

data class OverviewData(
    val formattedTotalSum: String = "",
    val gropedByTypesBills: List<BillTypeGrouped>? = null,
    val fmtPeriodOfTime: String = "",
)