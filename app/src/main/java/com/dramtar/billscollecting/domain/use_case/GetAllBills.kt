package com.dramtar.billscollecting.domain.use_case

import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypesRepository
import com.dramtar.billscollecting.domain.BillsRepository

class GetAllBills(
    private val billsRepository: BillsRepository,
    private val billTypesRepository: BillTypesRepository
) {
    suspend operator fun invoke(): List<BillData> {
        return billsRepository.getAllBills().map { billData ->
            billData.copy(
                billTypeData = billTypesRepository.getBillTypeById(
                    billData.billTypeData.id
                )
            )
        }
    }
}