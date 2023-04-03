package com.dramtar.billscollecting.presenter.type_overview

data class TypeChartData(
    val date: String,
    val sum: Double,
    val formattedSum: String,
    val percentage: Float,
    val formattedPercentage: String
)
