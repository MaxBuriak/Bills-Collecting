package com.dramtar.billscollecting.data.repository

import com.dramtar.billscollecting.data.local.bill.BillEntity
import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity
import com.dramtar.billscollecting.data.mapers.mapToBillData
import com.dramtar.billscollecting.data.mapers.mapToBillEntity
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.BillsRepository
import com.dramtar.billscollecting.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBillsRepository : BillsRepository {

    private val bills = mutableListOf<BillEntity>()
    private val billTypes = mutableListOf<BillTypeEntity>()

    override suspend fun getBills(start: Long, end: Long): Flow<List<BillData>> {
        return flow {
            emit(bills.filter { it.timestamp in start..end }.map { billEntity ->
                billEntity.mapToBillData(
                    billTypeData = BillTypeData(
                        billEntity.billTypeId ?: Constants.DELETED_TYPE
                    )
                )
            })
        }
    }

    override suspend fun saveBill(billData: BillData) {
        bills.add(billData.mapToBillEntity())
    }

    override suspend fun deleteBill(billData: BillData) {
        bills.remove(billData.mapToBillEntity())
    }

    override suspend fun getAllBills(): List<BillData> {
        return bills.map { billEntity ->
            billEntity.mapToBillData(
                billTypeData = BillTypeData(
                    billEntity.billTypeId ?: Constants.DELETED_TYPE
                )
            )
        }
    }

    override suspend fun getAllBillsByTypeID(typeData: BillTypeData): List<BillData> {
        return bills.filter { billEntity -> billEntity.billTypeId == typeData.id }
            .map { billEntity ->
                billEntity.mapToBillData(billTypeData = typeData)
            }
    }
}