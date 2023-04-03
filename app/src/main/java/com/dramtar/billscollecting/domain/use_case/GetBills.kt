package com.dramtar.billscollecting.domain.use_case

import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypesRepository
import com.dramtar.billscollecting.domain.BillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBills(
    private val billsRepository: BillsRepository,
    private val billTypesRepository: BillTypesRepository
) {
    suspend operator fun invoke(start: Long, end: Long): Flow<List<BillData>> {
        return billsRepository.getBills(start = start, end = end).map { list ->
            list.map { billData ->
                billData.copy(
                    billTypeData = billTypesRepository.getBillTypeById(
                        billData.billTypeData.id
                    )
                )
            }
        }
    }
}