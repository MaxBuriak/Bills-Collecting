package com.dramtar.billscollecting.data.local

import com.dramtar.billscollecting.data.local.bill.BillDao
import com.dramtar.billscollecting.data.local.bill.BillEntity
import com.dramtar.billscollecting.data.local.billtype.BillTypeDao
import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private var billDao: BillDao,
    private var billTypeDao: BillTypeDao
) : LocalDataSource {
    override suspend fun saveBill(billEntity: BillEntity) {
        billDao.saveBill(billEntity)
    }

    override suspend fun getBills(start: Long, end: Long): Flow<List<BillEntity>> {
        return billDao.getBills(start = start, end = end)
    }

    override suspend fun deleteBill(id: Int) {
        billDao.deleteBill(id)
    }

    override suspend fun saveBillType(billEntity: BillTypeEntity) {
        billTypeDao.saveBillType(billEntity)
    }

    override suspend fun getBillTypes(): Flow<List<BillTypeEntity>> {
        return billTypeDao.getBillTypes()
    }

    override suspend fun getBillTypeById(id: String): BillTypeEntity? {
        return billTypeDao.getBillTypeById(id = id)
    }

    override suspend fun updateBillTypeEntity(billEntity: BillTypeEntity) {
        billTypeDao.updateBillType(billEntity)
    }

    override suspend fun deleteBillType(id: String) {
        billTypeDao.deleteBill(id = id)
    }
}