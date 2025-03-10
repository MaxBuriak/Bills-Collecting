package com.dramtar.billscollecting.domain

import androidx.compose.ui.graphics.Color
import com.dramtar.billscollecting.utils.Constants
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class BillTypeData(
    val id: String = Constants.DELETED_TYPE,
    val name: String = Constants.DELETED_TYPE,
    @Transient val color: Color = Color.White,
    @Transient val invertedColor: Color = Color.Black,
    val priority: Int = 0
)

