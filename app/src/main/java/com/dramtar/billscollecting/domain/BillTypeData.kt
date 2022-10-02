package com.dramtar.billscollecting.domain

import androidx.compose.ui.graphics.Color

data class BillTypeData(
    val id: String = "",
    val name: String = "default",
    val color: Color = Color.Green,
    val invertedColor: Color = Color.White,
    val priority: Int = 0
)

