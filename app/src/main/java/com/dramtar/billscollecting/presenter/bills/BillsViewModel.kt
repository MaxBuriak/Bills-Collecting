package com.dramtar.billscollecting.presenter.bills

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dramtar.billscollecting.domain.*
import com.dramtar.billscollecting.domain.use_case.GetBills
import com.dramtar.billscollecting.presenter.UIEvent
import com.dramtar.billscollecting.presenter.utils.Utils
import com.dramtar.billscollecting.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BillsViewModel @Inject constructor(
    private val billsRepository: BillsRepository,
    private val billTypesRepository: BillTypesRepository,
    private val getBillsUseCase: GetBills
) : ViewModel() {
    var billListState by mutableStateOf(BillsState())
        private set
    private var billsJob: Job? = null

    init {
        getBills()
        getBillTypes()
    }

    fun onBillEvent(event: BillEvent) {
        when (event) {
            is BillEvent.Add -> {
                viewModelScope.launch {
                    val bill = BillData(
                        date = event.date,
                        billTypeData = billListState.selectedBillType,
                        amount = event.amount
                    )
                    billsRepository.saveBill(billData = bill)
                    increaseBillTypePriority(billListState.selectedBillType)
                }
            }
            is BillEvent.Delete -> {
                event.data.id?.let { id -> viewModelScope.launch { billsRepository.deleteBill(id) } }
            }
        }
    }

    fun onBillTypeEvent(event: BillTypeEvent) {
        when (event) {
            BillTypeEvent.Add -> {
                val newRndColor = Color.getRndColor()
                billListState = billListState.copy(
                    tmpBillType = BillTypeData(
                        name = "",
                        color = newRndColor,
                        invertedColor = newRndColor.getOnColor()
                    )
                )
            }
            is BillTypeEvent.Deleted -> {
                viewModelScope.launch {
                    billTypesRepository.deleteBillType(event.data.id)
                    if (billListState.selectedBillType.id == event.data.id) {
                        billListState = billListState.copy(selectedBillType = BillTypeData())
                    }
                }
            }
            is BillTypeEvent.Selected -> billListState =
                billListState.copy(selectedBillType = event.data)
            is BillTypeEvent.Complete -> {
                if (event.name.isBlank()) {
                    clearTmpBillType()
                    return
                }
                billListState.tmpBillType?.let { billType ->
                    viewModelScope.launch {
                        val type = billType.copy(
                            id = event.name.trim().replace(" ", "_").lowercase(),
                            name = event.name,
                        )
                        billTypesRepository.saveBillType(type)
                        onBillTypeEvent(BillTypeEvent.Selected(data = type))
                        clearTmpBillType()
                    }
                }
            }
        }
    }

    fun onUiEvent(event: UIEvent) {
        when (event) {
            is UIEvent.SelectDateRange -> {
                billListState = billListState.copy(selectedDateRange = event.date)
                getBills()
            }
        }
    }

    private suspend fun getGroupedByDayBillsList() {
        billListState = billListState
            .copy(gropedByDateBills = billListState.bills?.groupBy { it.date.getDayDayOfWeek() })
    }

    private fun setFirstTypeSelected() {
        if (billListState.selectedBillType.id != Constants.DELETED_TYPE) return
        billListState.billTypes.apply {
            if (this.isEmpty()) return
            billListState = billListState.copy(selectedBillType = this.first())
        }
    }

    private fun getBillTypes() {
        viewModelScope.launch {
            billTypesRepository.getBillTypes().cancellable().collectLatest { typesList ->
                billListState = billListState.copy(
                    billTypes = typesList.sortedByDescending { it.priority }
                )
                setFirstTypeSelected()
            }
        }
    }

    private suspend fun increaseBillTypePriority(billTypeData: BillTypeData) {
        billTypesRepository.updateBillType(billTypeData.copy(priority = billTypeData.priority.inc()))
    }

    private fun getBills() {
        val minMax = Utils.getMinMaxDate(
            selectedDateRange = billListState.selectedDateRange,
            calender = Calendar.getInstance()
        )
        billsJob?.cancel()
        billsJob = viewModelScope.launch {
            getBillsUseCase(start = minMax.min, end = minMax.max)
                .collectLatest { billsList ->
                    val sum = billsList.sumOf { it.amount }
                    billListState = billListState.copy(
                        bills = billsList,
                        totalSum = sum,
                        formattedTotalSum = sum.getFMTLocalCur()
                    )
                    getGroupedByDayBillsList()
                }
        }
    }

    private fun clearTmpBillType() {
        billListState = billListState.copy(tmpBillType = null)
    }
}