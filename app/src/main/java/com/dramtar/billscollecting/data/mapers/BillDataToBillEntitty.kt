package com.dramtar.billscollecting.data.mapers

import com.dramtar.billscollecting.data.local.bill.BillEntity
import com.dramtar.billscollecting.domain.BillData

fun BillData.mapToBillEntity() = BillEntity(
    id = id,
    timestamp = date,
    amount = amount,
    billTypeId = billTypeData.id
)