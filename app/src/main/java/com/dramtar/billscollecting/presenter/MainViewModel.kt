package com.dramtar.billscollecting.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.Repository
import com.dramtar.billscollecting.utils.getOnColor
import com.dramtar.billscollecting.utils.getRndColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    var billListState by mutableStateOf(BillsState())
        private set
    var colorState by mutableStateOf(Color(0))
        private set
    private var billsJob: Job? = null

    private val updatingEvent = Channel<String>()
    val updatingEvents = updatingEvent.receiveAsFlow()

    init {
        selectDateRange()
        getBillTypes()
    }

    fun onAddBillTypeButtonClick() {
        val newRndColor = Color.getRndColor()
        billListState = billListState.copy(
            tmpBillType = BillTypeData(
                name = "",
                color = newRndColor,
                invertedColor = newRndColor.getOnColor()
            )
        )
    }

    fun onCompleteBillTypeButtonClick(name: String) {
        if (name.isBlank()) {
            clearTmpBillType()
            return
        }
        billListState.tmpBillType?.let { billType ->
            billListState = billListState.copy(
                tmpBillType = billType.copy(
                    name = name
                )
            )
            addBillType()
        }
    }

    fun selectDateRange(date: Date = billListState.selectedDateRange) {
        val calender = Calendar.getInstance()
        calender.time = date
        billListState = billListState.copy(
            selectedDateRange = date
        )
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH))
        calender.set(Calendar.HOUR_OF_DAY, 0)
        val min = calender.timeInMillis
        calender.set(Calendar.HOUR_OF_DAY, 0)
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH))
        val max = calender.timeInMillis

        getBills(start = min, end = max)
    }

    private fun setFirstTypeSelected() {
        if (billListState.selectedBillTypeId.isNotBlank()) return
        billListState.billTypes.apply {
            if (this.isEmpty()) return
            billListState = billListState.copy(
                selectedBillTypeId = this[0].id
            )
        }
    }

    fun getBillTypes() {
        viewModelScope.launch {
            repository.getBillTypes().cancellable().collectLatest { typesList ->
                billListState = billListState.copy(
                    billTypes = typesList.sortedByDescending { it.priority }
                )
                setFirstTypeSelected()
            }
        }
    }

    private suspend fun increaseBillTypePriority(billTypeData: BillTypeData) {
        repository.updateBillType(billTypeData.copy(priority = billTypeData.priority.inc()))
    }

    fun getBills(start: Long, end: Long) {
        billsJob?.cancel()
        billsJob = viewModelScope.launch {
            repository.getBills(start = start, end = end).collectLatest { billsList ->
                billListState = billListState.copy(
                    bills = billsList,
                    totalSum = billsList.sumOf { it.amount }.roundToInt()
                )
            }
        }
    }

    fun billTypeSelected(id: String) {
        billListState = billListState.copy(selectedBillTypeId = id)
    }

    fun addBillType() {
        billListState.tmpBillType?.let { billType ->
            viewModelScope.launch {
                val type = billType.copy(
                    id = billType.name.trim().replace(" ", "_").lowercase(),
                    name = billType.name,
                )
                repository.saveBillType(type)
                billTypeSelected(type.id)
                clearTmpBillType()
            }
        }
    }

    private fun clearTmpBillType() { //TODO NEED REWORK
        billListState = billListState.copy(tmpBillType = null)
    }

    fun getBillType(id: String) {
        viewModelScope.launch {
            repository.getBillTypeById(id = id)
        }
    }

    fun addBill(amount: Double, date: Long) {
        viewModelScope.launch {
            val bill = BillData(
                date = date,
                billTypeData = BillTypeData(id = billListState.selectedBillTypeId),
                amount = amount
            )
            repository.saveBill(billData = bill)
            val billTypeData =
                billListState.billTypes.find { it.id == billListState.selectedBillTypeId }
            billTypeData?.let {
                increaseBillTypePriority(it)
            }
        }
    }
}