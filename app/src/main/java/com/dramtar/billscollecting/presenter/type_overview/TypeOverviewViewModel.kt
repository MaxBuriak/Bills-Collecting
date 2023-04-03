package com.dramtar.billscollecting.presenter.type_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.domain.BillTypesRepository
import com.dramtar.billscollecting.domain.BillsRepository
import com.dramtar.billscollecting.utils.Constants
import com.dramtar.billscollecting.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeOverviewViewModel @Inject constructor(
    private val billsRepository: BillsRepository,
    private val billTypesRepository: BillTypesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var typeOverviewState by mutableStateOf(TypeOverviewState())
        private set

    init {
        savedStateHandle.get<String>("typeId")?.let { id ->
            viewModelScope.launch {
                val type = billTypesRepository.getBillTypeById(id = id)
                getTypeOverview(type = type)
            }
        }
    }

    private suspend fun getTypeOverview(type: BillTypeData) {
        val bills = billsRepository.getAllBillsByTypeID(type)
        if (bills.isEmpty()) return //TODO need optimize and add some error
        val groupedList = getGroupedByMonthBillsList(bills)
            ?.takeLast(Constants.NUMBER_OF_OVERVIEW_ITEMS)
        val totalSum = bills.sumOf { it.amount }

        val startDate = bills.first().date.getMonthYear()
        val endDate = bills.last().date.getMonthYear()
        val fmtPeriodOfTime = if (startDate == endDate) startDate else "$startDate - $endDate"

        typeOverviewState = typeOverviewState.copy(
            type = type,
            gpdByDate = groupedList,
            sumTotal = totalSum,
            fmtSumTotal = totalSum.fmtLocalCurrency(),
            fmtPeriodOfTime = fmtPeriodOfTime,
        )
    }

    private suspend fun getGroupedByMonthBillsList(list: List<BillData>): List<TypeChartData>? {
        if (list.isEmpty()) return null //TODO need optimize need add error processing
        val groupedBills = list.groupBy { it.date.getMonth() }
        val maxSum = groupedBills.entries.maxOf { groupedList ->
            groupedList.value.sumOf { bill -> bill.amount }
        }
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
}