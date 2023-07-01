package com.dramtar.billscollecting.presenter.bills

import com.dramtar.billscollecting.data.repository.FakeBillTypesRepository
import com.dramtar.billscollecting.data.repository.FakeBillsRepository
import com.dramtar.billscollecting.domain.use_case.GetBills
import com.dramtar.billscollecting.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BillsViewModelTest {
    private lateinit var fakeBillsRepository: FakeBillsRepository
    private lateinit var fakeBillTypesRepository: FakeBillTypesRepository
    private lateinit var getBillsUseCase: GetBills
    private lateinit var billsViewModel: BillsViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeBillsRepository = FakeBillsRepository()
        fakeBillTypesRepository = FakeBillTypesRepository()
        getBillsUseCase = GetBills(
            billsRepository = fakeBillsRepository,
            billTypesRepository = fakeBillTypesRepository
        )
        billsViewModel = BillsViewModel(
            billsRepository = fakeBillsRepository,
            billTypesRepository = fakeBillTypesRepository,
            getBillsUseCase = getBillsUseCase
        )
    }

    @Test
    fun `addingNewBillType_added_tmpBillType_id_is_valid`() {
        billsViewModel.onBillTypeEvent(BillTypeEvent.Add)
        val tmpBillType = billsViewModel.billListState.tmpBillType
        assert(tmpBillType != null)
        assert(tmpBillType?.id != Constants.DELETED_TYPE)
    }
}