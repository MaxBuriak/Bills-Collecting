package com.dramtar.billscollecting.presenter

import com.dramtar.billscollecting.domain.BillTypeData

data class TypeOverviewData(
    val type: BillTypeData = BillTypeData(),
    val gpdByDate: Map<String, Pair<Double, String>>? = null,
    val sumTotal: Double = 0.0,
    val fmtSumTotal: String = "",
    val sumCurrMonth: Double = 0.0,
    val fmtSumCurrAmount: String = "",
    val currMonthPercentage: Float = 0F,
    val fmtCurrMonthPercentage: String = ""
)