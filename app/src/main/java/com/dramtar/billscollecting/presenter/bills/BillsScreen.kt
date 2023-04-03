package com.dramtar.billscollecting.presenter.bills

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dramtar.billscollecting.R
import com.dramtar.billscollecting.presenter.*
import com.dramtar.billscollecting.presenter.utils.composedatepicker.ComposeCalendar
import androidx.hilt.navigation.compose.hiltViewModel
import com.dramtar.billscollecting.presenter.bills.components.BillBottomBar
import com.dramtar.billscollecting.presenter.bills.components.BillItem
import com.dramtar.billscollecting.presenter.bills.components.DateStickyHeader
import com.dramtar.billscollecting.presenter.utils.Utils
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme
import com.dramtar.billscollecting.presenter.utils.composedatepicker.SelectDateListener
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun BillsScreen(
    navController: NavController,
    billsViewModel: BillsViewModel = hiltViewModel(),
    billAdded: () -> Unit
) {
    val billsState = billsViewModel.billListState
    val calendarShowing = remember { mutableStateOf(false) }
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()
    BillsCollectingTheme {
        Surface {
            Scaffold(topBar = {
                com.dramtar.billscollecting.presenter.bills.components.AppBar(
                    onNavigationClick = {},
                    billsState.formattedTotalSum,
                    onDateClick = { calendarShowing.value = true },
                    selectedRangeDate = billsState.selectedDateRange.time,
                    onChartsClick = {
                        val minMaxDate = Utils.getMinMaxDate(
                            selectedDateRange = billsState.selectedDateRange,
                            calender = Calendar.getInstance()
                        )
                        navController.navigate(
                            Screen.OverviewScreen.route
                                    + "?minDate=${minMaxDate.min}&maxDate=${minMaxDate.max}"
                        )
                    },
                    onRedChartsClick = {
                        navController.navigate(Screen.OverviewScreen.route)
                    }
                )
            }) { scaffoldPAdding ->
                scaffoldPAdding
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = 95.dp,
                    sheetContent = {
                        Surface(
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                        ) {
                            BillBottomBar(
                                billsState = billsState,
                                onAddBillButtonCLick = { amount, date ->
                                    billsViewModel.onBillEvent(
                                        BillEvent.Add(
                                            amount = amount,
                                            date = date
                                        )
                                    )
                                    scope.launch {
                                        if (sheetState.isExpanded) {
                                            scaffoldState.bottomSheetState.collapse()
                                        }
                                    }
                                    billAdded()
                                },
                                onBillTypeSelected = { data ->
                                    billsViewModel.onBillTypeEvent(
                                        BillTypeEvent.Selected(
                                            data = data
                                        )
                                    )
                                },
                                onAddBillTypeClick = {
                                    billsViewModel.onBillTypeEvent(BillTypeEvent.Add)
                                    /*scope.launch {
                                    if (sheetState.isCollapsed) sheetState.expand() else sheetState.collapse()
                                }*/
                                },
                                tmpBillType = billsState.tmpBillType,
                                onCompleteBillTypeClick = { name ->
                                    billsViewModel.onBillTypeEvent(BillTypeEvent.Complete(name = name))
                                },
                                onAmountClicked = {
                                    scope.launch {
                                        if (sheetState.isCollapsed) {
                                            scaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                },
                                onBillTypeDelete = {
                                    billsViewModel.onBillTypeEvent(BillTypeEvent.Deleted(data = it))
                                }
                            )
                        }
                    },
                    sheetBackgroundColor = Color.Transparent
                ) { scaffoldPadding ->
                    if (calendarShowing.value) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color = Color.Gray)
                                .padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 116.dp
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            ComposeCalendar(
                                initialDate = billsState.selectedDateRange,
                                title = stringResource(id = R.string.select_date_title),
                                listener = object : SelectDateListener {
                                    override fun onDateSelected(date: Date) {
                                        billsViewModel.onUiEvent(UIEvent.SelectDateRange(date = date))
                                        calendarShowing.value = false
                                    }

                                    override fun onCanceled() {
                                        calendarShowing.value = false
                                    }
                                })
                        }
                    }
                    Surface(color = MaterialTheme.colorScheme.background) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(scaffoldPadding)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1F),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                billsState.gropedByDateBills?.let { list ->
                                    list.forEach { (date, collectionsInAccount) ->
                                        stickyHeader { DateStickyHeader(formattedDate = date) }
                                        items(
                                            items = collectionsInAccount,
                                            key = { collection -> collection.id!! }
                                        ) { collection ->
                                            BillItem(
                                                data = collection,
                                                onItemClick = {},
                                                onDeleteButtonClick = { data ->
                                                    billsViewModel.onBillEvent(BillEvent.Delete(data))
                                                },
                                                onBillTypeClicked = {
                                                    navController.navigate(Screen.TypeOverviewScreen.route + "?typeId=${it.id}")
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}