package com.dramtar.billscollecting.data.repository

import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity
import com.dramtar.billscollecting.data.mapers.mapToBillTypeData
import com.dramtar.billscollecting.data.mapers.mapToBillTypeEntity
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.BillTypesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBillTypesRepository : BillTypesRepository {
    private val billTypes = mutableListOf<BillTypeEntity>()

    override suspend fun getBillTypes(): Flow<List<BillTypeData>> {
        return flow { emit(billTypes.map { billTypeEntity -> billTypeEntity.mapToBillTypeData() }) }
    }

    override suspend fun saveBillType(billTypeData: BillTypeData) {
        billTypes.add(billTypeData.mapToBillTypeEntity())
    }

    override suspend fun getBillTypeById(id: String): BillTypeData {
        return billTypes.find { it.id == id }?.mapToBillTypeData() ?: BillTypeData()
    }

    override suspend fun updateBillType(billTypeData: BillTypeData) {
        billTypes.removeIf { it.id == billTypeData.id }
        billTypes.add(billTypeData.mapToBillTypeEntity())
    }

    override suspend fun deleteBillType(id: String) {
        billTypes.removeIf { it.id == id }
    }
}