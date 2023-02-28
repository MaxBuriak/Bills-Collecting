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
import kotlin.math.ceil
import kotlin.math.pow

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
                        billTypeData = billListState.selectedBillType,
                        amount = event.amount
                    )
                    repository.saveBill(billData = bill)
                    increaseBillTypePriority(billListState.selectedBillType)
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
                        repository.saveBillType(type)
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
            is UIEvent.ExportToCSV -> exportBillsOverviewToCSVFile(event.file)
            is UIEvent.ShowAllBillsOverviewData -> getAllBillsOverviewData()
            is UIEvent.ShowCurrentMonthBillsOverviewData -> getCurrentMonthBillsOverviewData()
            is UIEvent.ShowTypeOverview -> getTypeOverview(event.type)
        }
    }

    fun getCSVFileName(): String = "${billListState.overviewData?.fmtPeriodOfTime} Bills overview.csv"

    private fun overviewData(bills: List<BillData>): OverviewData {
        val groupedBills = bills.groupBy { it.billTypeData }
        val totalSum = bills.sumOf { it.amount }
        val listOfSum = groupedBills.mapValues {
            it.value.sumOf { billData ->
                billData.amount
            }
        }.map {
            val percentage = (it.value / (totalSum)).toFloat()
            BillTypeGrouped(
                type = it.key,
                sumAmount = it.value,
                formattedSumAmount = it.value.fmtLocalCurrency(),
                percentage = percentage,
                formattedPercentage = percentage.fmtPercentage()
            )
        }

        val startDate = bills.first().date.getMonthYear()
        val endDate = bills.last().date.getMonthYear()
        val fmtPeriodOfTime = if (startDate == endDate) startDate else "$endDate - $startDate"

        return OverviewData(
            fmtPeriodOfTime = fmtPeriodOfTime,
            fmtTotalSum = totalSum.fmtLocalCurrency(),
            gropedByTypesBills = listOfSum.sortedByDescending { it.percentage })
    }

    private fun getAllBillsOverviewData() {
        viewModelScope.launch {
            billListState =
                billListState.copy(overviewData = overviewData(repository.getAllBills()))
            updatingEvent.send(UIUpdatingEvent.NavigateToOverview)
        }
    }

    private fun getCurrentMonthBillsOverviewData() {
        viewModelScope.launch {
            billListState.bills?.let { bills ->
                billListState = billListState.copy(
                    overviewData = overviewData(bills)
                )
                updatingEvent.send(UIUpdatingEvent.NavigateToOverview)
            }
        }
    }

    private suspend fun getGroupedByDayBillsList() {
        billListState =
            billListState.copy(gropedByDateBills = billListState.bills?.groupBy { it.date.getDayDayOfWeek() })
    }

    private suspend fun getGroupedByMonthBillsList(list: List<BillData>): List<TypeChartData>? {
        if (list.isEmpty()) return null //TODO need optimize need add error processing
        val groupedBills = list.groupBy { it.date.getMonth() }
        val maxSum =
            groupedBills.entries.maxOf { groupedList -> groupedList.value.sumOf { bill -> bill.amount } }
        return groupedBills.map {
            val sum = it.value.sumOf { bill -> bill.amount }
            val percentage = (sum / maxSum).toFloat() * 100
            TypeChartData(
                date = it.key,
                sum = sum,
                formattedSum = sum.fmtShortLocalCurrency(),
                percentage = percentage,
                formattedPercentage = percentage.fmtPercentage(),
            )
        }
    }

    private fun getTypeOverview(type: BillTypeData) {
        viewModelScope.launch {
            val bills = repository.getAllBillsByTypeID(type)
            if (bills.isEmpty()) return@launch //TODO need optimize and add some error
            val groupedList = getGroupedByMonthBillsList(bills)?.takeLast(6) //TODO MAKE constant
            val totalSum = bills.sumOf { it.amount }
            var currMonthSum = 0.0
            var currMonthsPercentage = 0F

            val startDate = bills.first().date.getMonthYear()
            val endDate = bills.last().date.getMonthYear()
            val fmtPeriodOfTime = if (startDate == endDate) startDate else "$startDate - $endDate"
            val maxSum = bills.maxOf { it.amount }

            val length = maxSum.toInt().toString().length / 2 //TODO NEED TO OPTIMIZE// need remove
            val multiplayer = 10.0.pow(length)
            val a = (maxSum / 2.0) / multiplayer.toInt()

            val separator = (ceil(a) * multiplayer)
            billListState.bills?.let { currBills ->
                val typeGrp =
                    overviewData(currBills).gropedByTypesBills?.find { it.type.id == type.id }
                typeGrp?.let {
                    currMonthSum = it.sumAmount
                    currMonthsPercentage = it.percentage
                }
            }
            //TODO check to remove it
            val data = TypeOverviewData(
                type = type,
                gpdByDate = groupedList,
                sumTotal = totalSum,
                fmtSumTotal = totalSum.fmtLocalCurrency(),
                sumCurrMonth = currMonthSum,
                fmtSumCurrAmount = currMonthSum.fmtLocalCurrency(),
                currMonthPercentage = currMonthsPercentage,
                fmtCurrMonthPercentage = currMonthsPercentage.fmtPercentage(),
                fmtPeriodOfTime = fmtPeriodOfTime,
                fmtCurrPeriodOfTime = billListState.selectedDateRange.time.getMonth(),
                maxSum = maxSum,
                separatorAmount = listOf(
                    (separator * 2).fmtLocalCurrency(),
                    separator.fmtLocalCurrency(),
                    (0).fmtLocalCurrency()
                )
            )
            billListState = billListState.copy(typeOverviewData = data)
            updatingEvent.send(UIUpdatingEvent.NavigateToTypeOverview)
        }
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
        if (billListState.selectedBillType.id != "deleted") return // TODO check to rework it
        billListState.billTypes.apply {
            if (this.isEmpty()) return
            billListState = billListState.copy(selectedBillType = this.first())
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
                        formattedTotalSum = sum.getFMTLocalCur()
                    )
                    getGroupedByDayBillsList()
                }
        }
    }

    private fun clearTmpBillType() {
        billListState = billListState.copy(tmpBillType = null)
    }

    private fun exportBillsOverviewToCSVFile(csvFile: File) {
        viewModelScope.launch {
            val overviewBillsList = billListState.overviewData?.gropedByTypesBills
            val formattedDate = billListState.overviewData?.fmtPeriodOfTime
            csvWriter().open(csvFile, append = false) {
                writeRow(listOf("", "Overview for $formattedDate"))
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
                writeRow(listOf("Total sum", billListState.overviewData?.fmtTotalSum))
            }
            updatingEvent.send(UIUpdatingEvent.OpenCreatedCSV(csvFile))
        }
    }
}