package com.dramtar.billscollecting.domain

import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getBills(start: Long, end: Long): Flow<List<BillData>>
    suspend fun saveBill(billData: BillData)
    suspend fun deleteBill(id: Int)

    suspend fun getBillTypes(): Flow<List<BillTypeData>>
    suspend fun saveBillType(billTypeData: BillTypeData)
    suspend fun getBillTypeById(id: String): BillTypeData
    suspend fun updateBillType(billTypeData: BillTypeData)
}