package com.dramtar.billscollecting.data.local.billtype

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bill_type_table")
data class BillTypeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val color: Int,
    val invertedColor: Int,
    val priority: Int
)