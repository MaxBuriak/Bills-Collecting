package com.dramtar.billscollecting.domain

import androidx.compose.ui.graphics.Color
import com.dramtar.billscollecting.utils.Constants

data class BillTypeData(
    val id: String = Constants.DELETED_TYPE,
    val name: String = Constants.DELETED_TYPE,
    val color: Color = Color.White,
    val invertedColor: Color = Color.Black,
    val priority: Int = 0
)

