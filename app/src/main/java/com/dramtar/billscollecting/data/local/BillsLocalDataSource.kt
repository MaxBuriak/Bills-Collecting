package com.dramtar.billscollecting.data.local

import com.dramtar.billscollecting.data.local.bill.BillEntity
import kotlinx.coroutines.flow.Flow

interface BillsLocalDataSource {
    suspend fun saveBill(billEntity: BillEntity)
    suspend fun getBills(start: Long, end: Long): Flow<List<BillEntity>>
    suspend fun deleteBill(billEntity: BillEntity)
    suspend fun getAllBill(): List<BillEntity>
    suspend fun getAllBillsByTypeID(typeID: String): List<BillEntity>
}