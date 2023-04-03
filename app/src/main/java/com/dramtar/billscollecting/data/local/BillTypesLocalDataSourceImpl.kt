package com.dramtar.billscollecting.data.local

import com.dramtar.billscollecting.data.local.billtype.BillTypeDao
import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BillTypesLocalDataSourceImpl @Inject constructor(
    private var billTypeDao: BillTypeDao
) : BillTypesLocalDataSource {
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
        billTypeDao.deleteBillType(id = id)
    }
}