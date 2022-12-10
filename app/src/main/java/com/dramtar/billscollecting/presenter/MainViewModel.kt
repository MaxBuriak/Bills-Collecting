package com.dramtar.billscollecting.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.BillTypeGrouped
import com.dramtar.billscollecting.domain.Repository
import com.dramtar.billscollecting.presenter.bill.BillEvent
import com.dramtar.billscollecting.presenter.bill.MinMaxDateInMilli
import com.dramtar.billscollecting.presenter.billType.BillTypeEvent
import com.dramtar.billscollecting.utils.*
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    var billListState by mutableStateOf(BillsState())
        private set
    private var billsJob: Job? = null

    private val updatingEvent = Channel<UIUpdatingEvent>()
    val updatingEvents = updatingEvent.receiveAsFlow()

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
                        billTypeData = BillTypeData(id = billListState.selectedBillTypeId),
                        amount = event.amount
                    )
                    repository.saveBill(billData = bill)
                    val type =
                        billListState.billTypes.find { it.id == billListState.selectedBillTypeId }
                    type?.let { increaseBillTypePriority(it) }
                    updatingEvent.send(UIUpdatingEvent.AddBillTypeClicked)
                }
            }
            is BillEvent.Delete -> {
                event.data.id?.let { id -> viewModelScope.launch { repository.deleteBill(id) } }
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
                    repository.deleteBillType(event.data.id)
                    if (billListState.selectedBillTypeId == event.data.id) {
                        billListState = billListState.copy(selectedBillTypeId = "")
                    }
                }
            }
            is BillTypeEvent.Selected -> billListState =
                billListState.copy(selectedBillTypeId = event.id)
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
                        repository.saveBillType(type)
                        onBillTypeEvent(BillTypeEvent.Selected(id = type.id))
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
            is UIEvent.ExportToCSV -> exportBillsOverviewToCSVFile(event.file)
            is UIEvent.ShowAllBillsOverview -> getAllBillsOverviewData()
        }
    }

    fun getCSVFileName(): String =
        "${billListState.bills?.get(0)?.date?.getMonthYear()} Bills overview.csv"

    private fun overviewData(bills: List<BillData>): List<BillTypeGrouped> {
        val groupedBills = bills.groupBy { it.billTypeData }
        val listOfSum = groupedBills.mapValues {
            it.value.sumOf { billData ->
                billData.amount
            }
        }.map {
            val percentage = (it.value / (billListState.totalSum)).toFloat()
            BillTypeGrouped(
                type = it.key,
                sumAmount = it.value,
                formattedSumAmount = it.value.getFMTLocalCurrency(),
                percentage = percentage,
                formattedPercentage = percentage.getFormattedPercentage()
            )
        }
        return listOfSum.sortedByDescending { it.percentage }
    }

    private fun getAllBillsOverviewData() {
        viewModelScope.launch {
            billListState = billListState.copy(overviewAllBillsTypes = overviewData(repository.getAllBills()))
        }
    }

    private suspend fun getGroupedByDateBillsList() {
        billListState = billListState.copy(gropedByDateBills = billListState.bills?.groupBy { it.date.getDayDayOfWeek() })
    }

    private fun getMinMaxDate(): MinMaxDateInMilli {
        val calender = Calendar.getInstance()
        calender.time = billListState.selectedDateRange
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH))
        calender.set(Calendar.HOUR_OF_DAY, 0)
        val min = calender.timeInMillis
        calender.set(Calendar.HOUR_OF_DAY, 0)
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH))
        val max = calender.timeInMillis
        return MinMaxDateInMilli(min = min, max = max)
    }

    private fun setFirstTypeSelected() {
        if (billListState.selectedBillTypeId.isNotBlank()) return
        billListState.billTypes.apply {
            if (this.isEmpty()) return
            billListState = billListState.copy(
                selectedBillTypeId = this.first().id
            )
        }
    }

    private fun getBillTypes() {
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

    private fun getBills() {
        val minMax = getMinMaxDate()
        billsJob?.cancel()
        billsJob = viewModelScope.launch {
            repository.getBills(start = minMax.min, end = minMax.max)
                .collectLatest { billsList ->
                    val sum = billsList.sumOf { it.amount }
                    billListState = billListState.copy(
                        bills = billsList,
                        totalSum = sum,
                        formattedTotalSum = sum.getFtdLocalCur()
                    )
                    billListState.bills?.let { bills ->
                        billListState = billListState.copy(
                            gropedTypesBills = overviewData(bills)
                        )
                    }
                    getGroupedByDateBillsList()
                }
        }
    }

    private fun clearTmpBillType() {
        billListState = billListState.copy(tmpBillType = null)
    }

    private fun exportBillsOverviewToCSVFile(csvFile: File) {
        viewModelScope.launch {
            val overviewBillsList = billListState.gropedTypesBills
            val formattedDate = billListState.bills?.get(0)?.date?.getMonthYear()
            csvWriter().open(csvFile, append = false) {
                writeRow(listOf("", "Overview of $formattedDate"))
                writeRow(listOf("Type", "Amount of payments", "Percentage"))
                overviewBillsList?.forEach { bill ->
                    writeRow(
                        listOf(
                            bill.type.name,
                            bill.formattedSumAmount,
                            bill.formattedPercentage
                        )
                    )
                }
                writeRow(listOf("Total sum", billListState.formattedTotalSum))
            }
            updatingEvent.send(UIUpdatingEvent.OpenCreatedCSV(csvFile))
        }
    }
}