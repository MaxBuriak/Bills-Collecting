package com.dramtar.billscollecting.data.local.bill

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bill_table")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val timestamp: Long,
    val amount: Double,
    val billTypeId: String?,
)