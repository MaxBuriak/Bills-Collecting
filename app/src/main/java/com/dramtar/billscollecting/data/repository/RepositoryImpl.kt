package com.dramtar.billscollecting.data.repository

import com.dramtar.billscollecting.data.local.LocalDataSource
import com.dramtar.billscollecting.data.mapers.mapToBillData
import com.dramtar.billscollecting.data.mapers.mapToBillEntity
import com.dramtar.billscollecting.data.mapers.mapToBillTypeData
import com.dramtar.billscollecting.data.mapers.mapToBillTypeEntity
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : Repository {
    override suspend fun getBills(start: Long, end: Long): Flow<List<BillData>> {
        return localDataSource.getBills(start = start, end = end).map { list ->
            list.map { billEntity ->
                billEntity.mapToBillData(
                    billTypeData = getBillTypeById(
                        billEntity.billTypeId ?: ""
                    )
                )
            }
        }
    }

    override suspend fun saveBill(billData: BillData) {
        localDataSource.saveBill(billData.mapToBillEntity())
    }

    override suspend fun deleteBill(id: Int) {
        localDataSource.deleteBill(id)
    }

    override suspend fun getBillTypes(): Flow<List<BillTypeData>> {
        return localDataSource.getBillTypes().map { list ->
            list.map { it.mapToBillTypeData() }
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