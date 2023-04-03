package com.dramtar.billscollecting.domain

import kotlinx.coroutines.flow.Flow

interface BillsRepository {
    @Deprecated("Use only in GetBills use case, need be mapped in right way")
    suspend fun getBills(start: Long, end: Long): Flow<List<BillData>>
    suspend fun saveBill(billData: BillData)
    suspend fun deleteBill(id: Int)
    @Deprecated("Use only in GetAllBills use case, need be mapped in right way")
    suspend fun getAllBills(): List<BillData>
    suspend fun getAllBillsByTypeID(typeData: BillTypeData): List<BillData>
}