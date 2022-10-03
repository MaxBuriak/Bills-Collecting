package com.dramtar.billscollecting.domain

data class BillData(
    val id: Int? = null,
    val date: Long = System.currentTimeMillis(),
    val billTypeData: BillTypeData = BillTypeData(),
    val amount: Double = 0.0,
    val formattedAmount: String = ""
)
