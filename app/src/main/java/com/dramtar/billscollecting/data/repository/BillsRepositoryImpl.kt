package com.dramtar.billscollecting.data.repository

import com.dramtar.billscollecting.data.local.BillsLocalDataSource
import com.dramtar.billscollecting.data.mapers.mapToBillData
import com.dramtar.billscollecting.data.mapers.mapToBillEntity
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.BillsRepository
import com.dramtar.billscollecting.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BillsRepositoryImpl @Inject constructor(
    private val localDataSource: BillsLocalDataSource
) : BillsRepository {
    override suspend fun getBills(start: Long, end: Long): Flow<List<BillData>> {
        return localDataSource.getBills(start = start, end = end).map { list ->
            list.map { billEntity ->
                billEntity.mapToBillData(
                    billTypeData = BillTypeData(
                        billEntity.billTypeId ?: Constants.DELETED_TYPE
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

    override suspend fun getAllBills(): List<BillData> {
        return localDataSource.getAllBill().map { billEntity ->
            billEntity.mapToBillData(
                billTypeData = BillTypeData(
                    billEntity.billTypeId ?: Constants.DELETED_TYPE
                )
            )
        }
    }

    override suspend fun getAllBillsByTypeID(typeData: BillTypeData): List<BillData> {
        return localDataSource.getAllBillsByTypeID(typeID = typeData.id).map { billEntity ->
            billEntity.mapToBillData(billTypeData = typeData)
        }
    }
}