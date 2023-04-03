package com.dramtar.billscollecting.domain

import kotlinx.coroutines.flow.Flow

interface BillTypesRepository {

    suspend fun getBillTypes(): Flow<List<BillTypeData>>
    suspend fun saveBillType(billTypeData: BillTypeData)
    suspend fun getBillTypeById(id: String): BillTypeData
    suspend fun updateBillType(billTypeData: BillTypeData)
    suspend fun deleteBillType(id: String)
}