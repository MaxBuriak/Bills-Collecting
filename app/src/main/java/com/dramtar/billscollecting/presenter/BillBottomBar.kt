package com.dramtar.billscollecting.presenter

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dramtar.billscollecting.R
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.utils.getDayMonthYear
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun BillBottomBar(
    onAddBillButtonCLick: (amount: Double, date: Long) -> Unit,
    onBillTypeSelected: (id: String) -> Unit,
    onAddBillTypeClick: () -> Unit,
    onCompleteBillTypeClick: (name: String) -> Unit,
    billsState: BillsState,
    tmpBillType: BillTypeData?,
    onAmountClicked: () -> Unit,
    onBillTypeDelete: (BillTypeData) -> Unit
) {
    val amountInputValue = remember { mutableStateOf(TextFieldValue()) }
    val billTypeName = remember { mutableStateOf(String()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar by remember { mutableStateOf(Calendar.getInstance()) }
    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf(System.currentTimeMillis().getDayMonthYear()) }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            mCalendar.set(year, month, dayOfMonth)
            mDate.value = mCalendar.time.time.getDayMonthYear()
        }, mYear, mMonth, mDay
    )
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = amountInputValue.value,
                    colors = TextFieldDefaults.textFieldColors(textColor = MaterialTheme.colorScheme.onSecondary),
                    label = {
                        Text(
                            text = stringResource(id = R.string.amount_hint),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }),
                    onValueChange = {
                        amountInputValue.value = it
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                        .onFocusChanged {
                            onAmountClicked()
                        },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .clickable { mDatePickerDialog.show() },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = mDate.value,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 16.sp
                    )
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = scrollState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(
                    items = billsState.billTypes,
                    key = { collection -> collection.id }
                ) { billTypeData ->
                    BillTypeItem(
                        data = billTypeData,
                        selectedBillTypeId = billsState.selectedBillTypeId,
                        onBillTypeSelected = onBillTypeSelected,
                        modifier = Modifier.height(73.dp),
                        onNameChanged = {},
                        onDeleteButtonClick = onBillTypeDelete
                    )
                }

                tmpBillType?.let { billType ->
                    item {
                        BillTypeItem(
                            data = billType,
                            onBillTypeSelected = {},
                            modifier = Modifier.height(73.dp),
                            onNameChanged = { billTypeName.value = it },
                            onDeleteButtonClick = {}
                        )
                    }
                }

                item {
                    val icon =
                        if (tmpBillType == null) Icons.Default.Add else Icons.Default.Done
                    Icon(
                        imageVector = icon,
                        contentDescription = "add button",
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = Color.White)
                            .size(65.dp)
                            .clickable {
                                tmpBillType?.let {
                                    onCompleteBillTypeClick(billTypeName.value)
                                } ?: run {
                                    onAddBillTypeClick()
                                    scope.launch {
                                        scrollState.animateScrollToItem(index = billsState.billTypes.size)
                                    }
                                }
                            }
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {
                    onAddBillButtonCLick(
                        amountInputValue.value.text.toDoubleOrNull() ?: 0.0,
                        mCalendar.time.time
                    )
                    amountInputValue.value = amountInputValue.value.copy(text = "")
                    focusManager.clearFocus()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(CircleShape)
            ) {
                Text(
                    text = stringResource(id = R.string.add_bill_button),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
@Preview
fun BottomBarPreview() {
    BillBottomBar(
        billsState = BillsState(
            billTypes = listOf(BillTypeData(), BillTypeData(), BillTypeData())
        ),
        onAddBillTypeClick = {},
        onBillTypeSelected = {},
        onAddBillButtonCLick = { _, _ -> },
        tmpBillType = null,
        onCompleteBillTypeClick = {},
        onAmountClicked = {},
        onBillTypeDelete = {}
    )
}