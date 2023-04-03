package com.dramtar.billscollecting.data.local

import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity
import kotlinx.coroutines.flow.Flow

interface BillTypesLocalDataSource {
    suspend fun saveBillType(billEntity: BillTypeEntity)
    suspend fun getBillTypes(): Flow<List<BillTypeEntity>>
    suspend fun getBillTypeById(id: String): BillTypeEntity?
    suspend fun updateBillTypeEntity(billEntity: BillTypeEntity)
    suspend fun deleteBillType(id: String)
}