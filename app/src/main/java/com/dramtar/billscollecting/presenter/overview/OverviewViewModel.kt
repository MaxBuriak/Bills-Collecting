package com.dramtar.billscollecting.presenter.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeGrouped
import com.dramtar.billscollecting.domain.use_case.GetAllBills
import com.dramtar.billscollecting.domain.use_case.GetBills
import com.dramtar.billscollecting.utils.fmtLocalCurrency
import com.dramtar.billscollecting.utils.fmtPercentage
import com.dramtar.billscollecting.utils.getMonthYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val getAllBills: GetAllBills,
    private val getBills: GetBills,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var overviewState by mutableStateOf(OverviewState())
        private set

    init {
        val minDate: Long = savedStateHandle.get<Long>("minDate") ?: -1L
        val maxDate: Long = savedStateHandle.get<Long>("maxDate") ?: -1L
        if (minDate > 0 && maxDate > 0) {
            getCurrentMonthBillsOverviewData(
                minDate = minDate,
                maxDate = maxDate
            )
        } else {
            getAllBillsOverviewData()
        }
    }

    private fun overviewData(bills: List<BillData>): OverviewState {
        if (bills.isEmpty()) return OverviewState()
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

        return OverviewState(
            fmtPeriodOfTime = fmtPeriodOfTime,
            fmtTotalSum = totalSum.fmtLocalCurrency(),
            gropedByTypesBills = listOfSum.sortedByDescending { it.percentage })
    }

    private fun getAllBillsOverviewData() {
        viewModelScope.launch { overviewState = overviewData(getAllBills.invoke()) }
    }

    private fun getCurrentMonthBillsOverviewData(minDate: Long, maxDate: Long) {
        viewModelScope.launch {
            getBills.invoke(start = minDate, end = maxDate).collectLatest { bills ->
                overviewState = overviewData(bills)
            }
        }
    }
}