package com.dramtar.billscollecting.presenter

import com.dramtar.billscollecting.domain.BillTypeGrouped

data class OverviewData(
    val fmtTotalSum: String = "",
    val gropedByTypesBills: List<BillTypeGrouped>? = null,
    val fmtPeriodOfTime: String = "",
)