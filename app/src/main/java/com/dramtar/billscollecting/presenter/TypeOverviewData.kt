package com.dramtar.billscollecting.presenter

import com.dramtar.billscollecting.domain.BillTypeData

data class TypeOverviewData(
    val type: BillTypeData = BillTypeData(),
    val gpdByDate: List<TypeChartData>? = null,
    val sumTotal: Double = 0.0,
    val fmtSumTotal: String = "",
    val sumCurrMonth: Double = 0.0,
    val fmtSumCurrAmount: String = "",
    val currMonthPercentage: Float = 0F,
    val fmtCurrMonthPercentage: String = "",
    val fmtPeriodOfTime: String = "",
    val maxSum: Double = 0.0,
    val separatorAmount:List<String> = listOf()
)