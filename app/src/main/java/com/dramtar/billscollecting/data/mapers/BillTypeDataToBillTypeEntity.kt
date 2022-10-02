package com.dramtar.billscollecting.data.mapers

import androidx.compose.ui.graphics.toArgb
import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity
import com.dramtar.billscollecting.domain.BillTypeData

fun BillTypeData.mapToBillTypeEntity() = BillTypeEntity(
    id = id,
    name = name,
    color = color.toArgb(),
    invertedColor = invertedColor.toArgb(),
    priority = priority
)