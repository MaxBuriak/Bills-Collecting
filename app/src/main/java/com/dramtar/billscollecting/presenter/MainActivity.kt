package com.dramtar.billscollecting.presenter

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.dramtar.billscollecting.presenter.composedatepicker.ComposeCalendar
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme
import com.dramtar.billscollecting.utils.getDayDayOfWeek
import com.dt.composedatepicker.SelectDateListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        lifecycleScope.launchWhenStarted {
            viewModel.updatingEvents.collectLatest {
                Toast.makeText(this@MainActivity, "$it already updated!", Toast.LENGTH_SHORT).show()
            }
        }
        setContent {
            val calendarShowing = remember { mutableStateOf(false) }
            BillsCollectingTheme {
                Surface {
                    Scaffold(topBar = {
                        AppBar(
                            onNavigationClick = {},
                            viewModel.billListState.totalSum,
                            onDateClick = { calendarShowing.value = true },
                            selectedRangeDate = viewModel.billListState.selectedDateRange.time
                        )
                    }) { scaffoldPAdding ->
//                        Box(modifier = Modifier.size(100.dp).background(viewModel.colorState)) {
//                            //Button(onClick = { /*viewModel.getColor()*/},modifier = Modifier.align(Alignment.Center)) {
//                                Text(
//                                    text = "click",
//                                    color = viewModel.colorState.getOnColor(),
//                                    fontSize = 32.sp
//
//                                )
//                            //}
//                        }
                        scaffoldPAdding
                        val sheetState = rememberBottomSheetState(
                            initialValue = BottomSheetValue.Collapsed
                        )
                        val scaffoldState = rememberBottomSheetScaffoldState(
                            bottomSheetState = sheetState,
                        )
                        val scope = rememberCoroutineScope()
                        BottomSheetScaffold(
                            scaffoldState = scaffoldState,
                            sheetPeekHeight = 100.dp,
                            sheetContent = {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                                ) {
                                    BillBottomBar(
                                        onCalendarClick = {},
                                        billsState = viewModel.billListState,
                                        onAddBillButtonCLick = { amount, date ->
                                            viewModel.addBill(
                                                amount = amount,
                                                date = date
                                            )
                                            scope.launch {
                                                if (sheetState.isExpanded) {
                                                    scaffoldState.bottomSheetState.collapse()
                                                }
                                            }
                                        },
                                        onBillTypeSelected = { id ->
                                            viewModel.billTypeSelected(id)
                                        },
                                        onAddBillTypeClick = {
                                            viewModel.onAddBillTypeButtonClick()
                                            /*scope.launch {
                                            if (sheetState.isCollapsed) sheetState.expand() else sheetState.collapse()
                                        }*/
                                        },
                                        tmpBillType = viewModel.billListState.tmpBillType,
                                        onCompleteBillTypeClick = { name ->
                                            viewModel.onCompleteBillTypeButtonClick(name)
                                        },
                                        onAmountClicked = {
                                            scope.launch {
                                                if (sheetState.isCollapsed) {
                                                    scaffoldState.bottomSheetState.expand()
                                                }
                                            }
                                        }
                                    )
                                }
                            },
                            sheetBackgroundColor = Color.Transparent
                        ) { scaffoldPadding ->

                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.YEAR, 2010)
                            calendar.set(Calendar.MONTH, 6)
                            val calendarMax = Calendar.getInstance()
                            calendarMax.set(Calendar.YEAR, 2032)
                            calendarMax.set(Calendar.MONTH, 9)

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
                                    ComposeCalendar(minDate = calendar.time,
                                        maxDate = calendarMax.time,
                                        locale = Locale("en"),
                                        initialDate = viewModel.billListState.selectedDateRange,
                                        title = "Select Date",
                                        listener = object : SelectDateListener {
                                            override fun onDateSelected(date: Date) {
                                                viewModel.selectDateRange(date)
                                                calendarShowing.value = false
                                            }

                                            override fun onCanceled() {
                                                calendarShowing.value = false
                                            }
                                        })
                                }
                            }
                            val grouped =
                                viewModel.billListState.bills?.groupBy { it.date.getDayDayOfWeek() }

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
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        viewModel.billListState.bills?.let {
                                            grouped?.forEach { (date, collectionsInAccount) ->
                                                stickyHeader {
                                                    Box(modifier = Modifier.fillMaxWidth().padding(
                                                        top = 16.dp,
                                                        start = 8.dp,
                                                        end = 16.dp,
                                                        bottom = 8.dp
                                                    )) {
                                                        Box(
                                                            modifier = Modifier
                                                                .clip(
                                                                    RoundedCornerShape(10.dp)
                                                                )
                                                                .background(MaterialTheme.colorScheme.tertiary)
                                                                .align(Center)
                                                        ) {
                                                            Text(
                                                                date,
                                                                color = MaterialTheme.colorScheme.onTertiary,
                                                                style = MaterialTheme.typography.titleLarge,
                                                                fontWeight = FontWeight.Bold,
                                                                modifier = Modifier
                                                                    .align(Center)
                                                                    .padding(6.dp)
                                                            )
                                                        }
                                                    }
                                                }

                                                items(
                                                    items = collectionsInAccount,
                                                    key = { collection -> collection.id!! }
                                                ) { collection ->
                                                    BillItem(
                                                        item = collection,
                                                        onItemClick = {})
                                                    Divider(
                                                        color = Color.Gray,
                                                        modifier = Modifier.padding(horizontal = 16.dp)
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


@Preview
@Composable
fun MainPreview() {
    BillsCollectingTheme {
        /*Scaffold(topBar = { AppBar(onNavigationClick = { }) }) {
            it
            //Content()
        }*/
    }
}
