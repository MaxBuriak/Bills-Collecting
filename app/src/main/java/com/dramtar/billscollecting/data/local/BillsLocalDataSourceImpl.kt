package com.dramtar.billscollecting.data.local

import com.dramtar.billscollecting.data.local.bill.BillDao
import com.dramtar.billscollecting.data.local.bill.BillEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BillsLocalDataSourceImpl @Inject constructor(
    private var billDao: BillDao,
) : BillsLocalDataSource {
    override suspend fun saveBill(billEntity: BillEntity) {
        billDao.saveBill(billEntity)
    }

    override suspend fun getBills(start: Long, end: Long): Flow<List<BillEntity>> {
        return billDao.getBills(start = start, end = end)
    }

    override suspend fun deleteBill(billEntity: BillEntity) {
        billDao.deleteBill(billEntity)
    }

    override suspend fun getAllBill(): List<BillEntity> {
        return billDao.getAllBills()
    }

    override suspend fun getAllBillsByTypeID(typeID: String): List<BillEntity> {
        return billDao.getAllBillsByTypeID(typeID = typeID)
    }
}