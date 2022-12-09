package com.dramtar.billscollecting.data.mapers

import com.dramtar.billscollecting.data.local.bill.BillEntity
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.utils.getFormattedLocalCurrency

fun BillEntity.mapToBillData(billTypeData: BillTypeData = BillTypeData()) = BillData(
    id = id,
    date = timestamp,
    amount = amount,
    billTypeData = billTypeData,
    formattedAmount = amount.getFormattedLocalCurrency()
)