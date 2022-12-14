package com.dramtar.billscollecting.presenter

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
import com.dramtar.billscollecting.presenter.bill.BillEvent
import com.dramtar.billscollecting.presenter.billType.BillTypeEvent
import com.dramtar.billscollecting.presenter.composedatepicker.ComposeCalendar
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme
import com.dt.composedatepicker.SelectDateListener
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val calendarShowing = remember { mutableStateOf(false) }
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()
    BillsCollectingTheme {
        Surface {
            Scaffold(topBar = {
                AppBar(
                    onNavigationClick = {},
                    viewModel.billListState.formattedTotalSum,
                    onDateClick = { calendarShowing.value = true },
                    selectedRangeDate = viewModel.billListState.selectedDateRange.time,
                    onChartsClick = { viewModel.onUiEvent(UIEvent.ShowCurrentMonthBillsOverviewData) },
                    onRedChartsClick = { viewModel.onUiEvent(UIEvent.ShowAllBillsOverviewData) }
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
                                billsState = viewModel.billListState,
                                onAddBillButtonCLick = { amount, date ->
                                    viewModel.onBillEvent(
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
                                },
                                onBillTypeSelected = { id ->
                                    viewModel.onBillTypeEvent(
                                        BillTypeEvent.Selected(
                                            id = id
                                        )
                                    )
                                },
                                onAddBillTypeClick = {
                                    viewModel.onBillTypeEvent(BillTypeEvent.Add)
                                    /*scope.launch {
                                    if (sheetState.isCollapsed) sheetState.expand() else sheetState.collapse()
                                }*/
                                },
                                tmpBillType = viewModel.billListState.tmpBillType,
                                onCompleteBillTypeClick = { name ->
                                    viewModel.onBillTypeEvent(BillTypeEvent.Complete(name = name))
                                },
                                onAmountClicked = {
                                    scope.launch {
                                        if (sheetState.isCollapsed) {
                                            scaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                },
                                onBillTypeDelete = {
                                    viewModel.onBillTypeEvent(BillTypeEvent.Deleted(data = it))
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
                                initialDate = viewModel.billListState.selectedDateRange,
                                title = stringResource(id = R.string.select_date_title),
                                listener = object : SelectDateListener {
                                    override fun onDateSelected(date: Date) {
                                        viewModel.onUiEvent(UIEvent.SelectDateRange(date = date))
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
                                viewModel.billListState.gropedByDateBills?.let { list ->
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
                                                    viewModel.onBillEvent(BillEvent.Delete(data))
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