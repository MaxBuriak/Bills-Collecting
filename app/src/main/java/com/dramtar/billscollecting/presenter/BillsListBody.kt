package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun BillsListBody(
    billsState: BillsState,
    billList: List<BillData>?,
    onAddBillButtonCLick: (amount: Double, date: Long) -> Unit,
    onCalendarClick: () -> Unit,
    onBillTypeSelected: (id: String) -> Unit,
    onAddBillTypeClick: (billName: String, billColor: Long) -> Unit,
) {
    val billTypeNameValue = remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState,
    )
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = billTypeNameValue.value,

                    label = {
                        Text(
                            text = "Bill type name",
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    onValueChange = { billTypeNameValue.value = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = {
                        scope.launch {
                            onAddBillTypeClick(
                                billTypeNameValue.value.text,
                                0 //NEED IMPLEMENT COLOR PICKER
                            )
                            billTypeNameValue.value = billTypeNameValue.value.copy(text = "")
                            sheetState.collapse()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    Text(
                        text = "Add",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
        sheetBackgroundColor = Color.Yellow
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            billList?.let {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(it) { bill ->
                        BillItem(
                            item = bill,
                            onItemClick = {})
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
            /*BillBottomBar(
                onCalendarClick = onCalendarClick,
                billsState = billsState,
                onAddBillButtonCLick = onAddBillButtonCLick,
                onBillTypeSelected = onBillTypeSelected,
                onAddBillTypeClick = {
                    scope.launch {
                        if (sheetState.isCollapsed) sheetState.expand() else sheetState.collapse()
                    }
                }
            )*/
        }
    }
}

private val dummyList = listOf(
    BillData(billTypeData = BillTypeData(), date = 0, amount = 0.0),
    BillData(billTypeData = BillTypeData(), date = 0, amount = 0.0),
    BillData(billTypeData = BillTypeData(), date = 0, amount = 0.0),
)

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
@Preview
fun ContentPreview() {
    BillsCollectingTheme {
        Surface {
            BillsListBody(
                billsState = BillsState(),
                billList = dummyList,
                onAddBillButtonCLick = { _, _ -> },
                onCalendarClick = {},
                onAddBillTypeClick = { _, _ -> },
                onBillTypeSelected = {},
            )
        }
    }
}