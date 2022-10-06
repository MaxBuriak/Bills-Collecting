package com.dramtar.billscollecting.data.local

import com.dramtar.billscollecting.data.local.bill.BillEntity
import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveBill(billEntity: BillEntity)
    suspend fun getBills(start: Long, end: Long): Flow<List<BillEntity>>
    suspend fun deleteBill(id: Int)

    suspend fun saveBillType(billEntity: BillTypeEntity)
    suspend fun getBillTypes(): Flow<List<BillTypeEntity>>
    suspend fun getBillTypeById(id: String): BillTypeEntity?
    suspend fun updateBillTypeEntity(billEntity: BillTypeEntity)
}