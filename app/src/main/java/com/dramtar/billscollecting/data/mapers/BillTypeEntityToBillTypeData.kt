package com.dramtar.billscollecting.data.mapers

import androidx.compose.ui.graphics.Color
import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity
import com.dramtar.billscollecting.domain.BillTypeData

fun BillTypeEntity.mapToBillTypeData() = BillTypeData(
    id = id,
    name = name,
    color = Color(color),
    invertedColor = Color(invertedColor),
    priority = priority
)