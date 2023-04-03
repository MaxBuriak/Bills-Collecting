package com.dramtar.billscollecting.data.repository

import com.dramtar.billscollecting.data.local.BillTypesLocalDataSource
import com.dramtar.billscollecting.data.mapers.mapToBillTypeData
import com.dramtar.billscollecting.data.mapers.mapToBillTypeEntity
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.BillTypesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BillTypesRepositoryImpl @Inject constructor(
    private val localDataSource: BillTypesLocalDataSource
) : BillTypesRepository {
    override suspend fun getBillTypes(): Flow<List<BillTypeData>> {
        return localDataSource.getBillTypes().map { list ->
            list.map { billTypeEntity ->
                billTypeEntity.mapToBillTypeData()
            }
        }
    }

    override suspend fun saveBillType(billTypeData: BillTypeData) {
        localDataSource.saveBillType(billEntity = billTypeData.mapToBillTypeEntity())
    }

    override suspend fun getBillTypeById(id: String): BillTypeData {
        return localDataSource.getBillTypeById(id = id)?.mapToBillTypeData() ?: BillTypeData()
    }

    override suspend fun updateBillType(billTypeData: BillTypeData) {
        localDataSource.updateBillTypeEntity(billEntity = billTypeData.mapToBillTypeEntity())
    }

    override suspend fun deleteBillType(id: String) {
        localDataSource.deleteBillType(id = id)
    }
}