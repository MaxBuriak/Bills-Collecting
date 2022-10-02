package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dramtar.billscollecting.domain.BillTypeData

@ExperimentalComposeUiApi
@Composable
fun BillTypeItem(
    billType: BillTypeData,
    modifier: Modifier = Modifier,
    selectedBillTypeId: String = "",
    onBillTypeSelected: (id: String) -> Unit,
    onNameChanged: (name: String) -> Unit
) {
    val nameInputValue = remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (billType.name.isBlank()) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(billType.color)
                .selectable(
                    selected = selectedBillTypeId == billType.id,
                    onClick = { onBillTypeSelected(billType.id) }
                )
                .padding(vertical = 6.dp, horizontal = 3.dp),
            contentAlignment = Alignment.Center
        ) {

            val focusRequester = remember { FocusRequester() }
            TextField(
                value = nameInputValue.value,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                onValueChange = {
                    nameInputValue.value = it
                    onNameChanged(it.text)
                },
                modifier = Modifier
                    .defaultMinSize(minWidth = 70.dp)
                    .padding(horizontal = 4.dp)
                    .focusRequester(focusRequester),
                textStyle = TextStyle(
                    color = billType.invertedColor,
                    fontSize = 18.sp
                ),
                singleLine = true,
                maxLines = 1,
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    } else {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(billType.color)
                .selectable(
                    selected = selectedBillTypeId == billType.id,
                    onClick = { onBillTypeSelected(billType.id) }
                )
                .padding(vertical = 6.dp, horizontal = 3.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = billType.name,
                fontSize = 18.sp,
                color = billType.invertedColor,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )

            if (billType.id == selectedBillTypeId) {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = billType.invertedColor,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
@Preview
fun BillTypePreview() {
    BillTypeItem(
        billType = BillTypeData(name = ""),
        modifier = Modifier.height(65.dp),
        onBillTypeSelected = {},
        onNameChanged = {}
    )
}