package com.dramtar.billscollecting.data.repository

import com.dramtar.billscollecting.data.local.bill.BillEntity
import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity
import com.dramtar.billscollecting.data.mapers.mapToBillData
import com.dramtar.billscollecting.data.mapers.mapToBillEntity
import com.dramtar.billscollecting.data.mapers.mapToBillTypeData
import com.dramtar.billscollecting.data.mapers.mapToBillTypeEntity
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : Repository {

    private val bills = mutableListOf<BillEntity>()
    private val billTypes = mutableListOf<BillTypeEntity>()

    override suspend fun getBills(start: Long, end: Long): Flow<List<BillData>> {
        return flow {
            emit(bills.filter { it.timestamp in start..end }.map { billEntity ->
                billEntity.mapToBillData(
                    billTypeData = getBillTypeById(
                        billEntity.billTypeId ?: ""
                    )
                )
            })
        }
    }

    override suspend fun saveBill(billData: BillData) {
        bills.add(billData.mapToBillEntity())
    }

    override suspend fun deleteBill(id: Int) {
        bills.remove(bills.find { it.id == id }) //TODO REWRITE IT
    }

    override suspend fun getAllBills(): List<BillData> {
        return bills.map { billEntity ->
            billEntity.mapToBillData(
                billTypeData = getBillTypeById(
                    billEntity.billTypeId ?: ""
                )
            )
        }
    }

    override suspend fun getAllBillsByTypeID(typeData: BillTypeData): List<BillData> {
        return bills.filter { billEntity -> billEntity.billTypeId == typeData.id }
            .map { billEntity ->
                billEntity.mapToBillData(
                    billTypeData = getBillTypeById(
                        billEntity.billTypeId ?: ""
                    )
                )
            }
    }

    override suspend fun getBillTypes(): Flow<List<BillTypeData>> {
        return flow { emit(billTypes.map { billTypeEntity -> billTypeEntity.mapToBillTypeData() }) }
    }

    override suspend fun saveBillType(billTypeData: BillTypeData) {
        billTypes.add(billTypeData.mapToBillTypeEntity())
    }

    override suspend fun getBillTypeById(id: String): BillTypeData {
        return billTypes.find { it.id == id }?.mapToBillTypeData() ?: BillTypeData() //TODO FIX IT
    }

    override suspend fun updateBillType(billTypeData: BillTypeData) {
        billTypes.removeIf { it.id == billTypeData.id }
        billTypes.add(billTypeData.mapToBillTypeEntity())
    }

    override suspend fun deleteBillType(id: String) {
        billTypes.removeIf { it.id == id }
    }
}