package com.dramtar.billscollecting.domain

data class BillTypeGrouped(
    val type: BillTypeData,
    val sumAmount: Double,
    val formattedSumAmount: String,
    val percentage: Float,
    val formattedPercentage: String
)
