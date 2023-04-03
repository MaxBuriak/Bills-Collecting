package com.dramtar.billscollecting.presenter.overview

import com.dramtar.billscollecting.domain.BillTypeGrouped

data class OverviewState(
    val fmtTotalSum: String = "",
    val gropedByTypesBills: List<BillTypeGrouped>? = null,
    val fmtPeriodOfTime: String = "",
)