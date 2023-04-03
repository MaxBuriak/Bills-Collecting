package com.dramtar.billscollecting.presenter.type_overview

import com.dramtar.billscollecting.domain.BillTypeData

data class TypeOverviewState(
    val type: BillTypeData = BillTypeData(),
    val gpdByDate: List<TypeChartData>? = null,
    val sumTotal: Double = 0.0,
    val fmtSumTotal: String = "",
    val fmtPeriodOfTime: String = "",
    val maxSum: Double = 0.0,
    val separatorAmount: List<String> = listOf()
)