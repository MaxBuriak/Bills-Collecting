@file:OptIn(ExperimentalComposeUiApi::class)

package com.dramtar.billscollecting.presenter.bills

import BillsCollectingTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dramtar.billscollecting.R
import com.dramtar.billscollecting.presenter.*
import com.dramtar.billscollecting.presenter.utils.composedatepicker.ComposeCalendar
import androidx.hilt.navigation.compose.hiltViewModel
import com.dramtar.billscollecting.presenter.bills.components.AppBar
import com.dramtar.billscollecting.presenter.bills.components.BillBottomBar
import com.dramtar.billscollecting.presenter.bills.components.BillItem
import com.dramtar.billscollecting.presenter.bills.components.DateStickyHeader
import com.dramtar.billscollecting.presenter.utils.Utils
import com.dramtar.billscollecting.presenter.utils.composedatepicker.SelectDateListener
import com.dramtar.billscollecting.utils.getMonthYear
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
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
    var calendarShowing by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    BillsCollectingTheme {
        Surface {
            Scaffold { scaffoldPAdding ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(scaffoldPAdding)
                        .consumeWindowInsets(scaffoldPAdding)
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
                ) {
                    AppBar(
                        onNavigationClick = {},
                        billsState.formattedTotalSum,
                        onDateClick = {
                            calendarShowing = true
                        },
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
                    BottomSheetScaffold(
                        scaffoldState = scaffoldState,
                        sheetPeekHeight = 95.dp,
                        sheetDragHandle = null,
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
                                            if (scaffoldState.bottomSheetState.isVisible) {
                                                scaffoldState.bottomSheetState.hide()
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
                                            if (scaffoldState.bottomSheetState.isVisible) {
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
                    ) { scaffoldPadding ->

                        Surface(color = MaterialTheme.colorScheme.background) {
                            if (calendarShowing) {
                                Surface {
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
                                                    billsViewModel.onUiEvent(
                                                        UIEvent.SelectDateRange(
                                                            date = date
                                                        )
                                                    )
                                                    calendarShowing = false
                                                }

                                                override fun onCanceled() {
                                                    calendarShowing = false
                                                }
                                            })
                                    }
                                }
                            } else {
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
                                                            billsViewModel.onBillEvent(
                                                                BillEvent.Delete(
                                                                    data
                                                                )
                                                            )
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
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    navController: NavController,
    billsViewModel: BillsViewModel = hiltViewModel(),
    billAdded: () -> Unit
) {
    val billsState = billsViewModel.billListState
    var calendarShowing by remember { mutableStateOf(false) }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    BillsCollectingTheme {
        Surface {
            Scaffold { scaffoldPAdding ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(scaffoldPAdding)
                        .consumeWindowInsets(scaffoldPAdding)
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
                ) {
                    if (calendarShowing) {
                        BasicAlertDialog(
                            onDismissRequest = { calendarShowing = false }) {
                            Surface {
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
                                                billsViewModel.onUiEvent(
                                                    UIEvent.SelectDateRange(
                                                        date = date
                                                    )
                                                )
                                                calendarShowing = false
                                            }

                                            override fun onCanceled() {
                                                calendarShowing = false
                                            }
                                        })
                                }
                            }
                        }
                    }

                    Column {
                        TotalSumCard(
                            totalSum = billsState.formattedTotalSum,
                            selectedDateRange = billsState.selectedDateRange.time.getMonthYear(),
                            onCardClick = {
                                val minMaxDate = Utils.getMinMaxDate(
                                    selectedDateRange = billsState.selectedDateRange,
                                    calender = Calendar.getInstance()
                                )
                                navController.navigate(
                                Screen.OverviewScreen.route
                                        + "?minDate=${minMaxDate.min}&maxDate=${minMaxDate.max}"
                            ) },
                            onDateClick = { calendarShowing = true }
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 78.dp),
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

                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.secondary,
                        onClick = { openBottomSheet = true }
                    ) {
                        Text(
                            text = stringResource(R.string.add_bill_button),
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }

                    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    if (openBottomSheet) {
                        ModalBottomSheet(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            sheetState = bottomSheetState,
                            onDismissRequest = { openBottomSheet = false }
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
                                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                        if (!bottomSheetState.isVisible) {
                                            openBottomSheet = false
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
                                onAddBillTypeClick = { billsViewModel.onBillTypeEvent(BillTypeEvent.Add) },
                                tmpBillType = billsState.tmpBillType,
                                onCompleteBillTypeClick = { name ->
                                    billsViewModel.onBillTypeEvent(BillTypeEvent.Complete(name = name))
                                },
                                onAmountClicked = { },
                                onBillTypeDelete = {
                                    billsViewModel.onBillTypeEvent(BillTypeEvent.Deleted(data = it))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TotalSumCard(
    totalSum: String,
    selectedDateRange: String,
    onCardClick: () -> Unit,
    onDateClick: () -> Unit
) {
    Card(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().clickable { onCardClick() }.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                totalSum,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                modifier = Modifier.padding(top = 12.dp).clickable { onDateClick() },
                text = selectedDateRange,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TotalSumCardPreview() {
    BillsCollectingTheme {
        TotalSumCard(totalSum = "$1000.00", selectedDateRange = "Mar 2025", onCardClick = {}, onDateClick = {})
    }
}