package com.dramtar.billscollecting.domain

import androidx.compose.ui.graphics.Color

data class BillTypeData(
    val id: String = "deleted",
    val name: String = "DELETED",
    val color: Color = Color.White,
    val invertedColor: Color = Color.Black,
    val priority: Int = 0
)

