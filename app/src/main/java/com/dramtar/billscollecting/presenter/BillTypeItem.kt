package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dramtar.billscollecting.domain.BillTypeData

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun BillTypeItem(
    data: BillTypeData,
    modifier: Modifier = Modifier,
    selectedBillTypeId: String = "",
    onBillTypeSelected: (id: String) -> Unit,
    onNameChanged: (name: String) -> Unit,
    onDeleteButtonClick: (BillTypeData) -> Unit
) {

    val deleteButtonState = remember { mutableStateOf(false) }
    val nameInputValue = remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier,
        contentAlignment = Alignment.Center) {
        if (data.name.isBlank()) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(data.color)
                    .selectable(
                        selected = selectedBillTypeId == data.id,
                        onClick = { onBillTypeSelected(data.id) },
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
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    onValueChange = {
                        nameInputValue.value = it
                        onNameChanged(it.text)
                    },
                    modifier = Modifier
                        .defaultMinSize(minWidth = 70.dp)
                        .padding(horizontal = 4.dp)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        color = data.invertedColor, fontSize = 18.sp
                    ),
                    singleLine = true,
                    maxLines = 1,
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(data.color)
                    .padding(vertical = 6.dp, horizontal = 3.dp)
                    .selectable(
                        selected = selectedBillTypeId == data.id,
                        onClick = {},
                    )
                    .combinedClickable(onClick = {
                        if (!deleteButtonState.value) onBillTypeSelected(data.id)
                        deleteButtonState.value = false
                    }, onLongClick = { deleteButtonState.value = true }),

                contentAlignment = Alignment.Center
            ) {
                Row {
                    Text(
                        text = data.name,
                        fontSize = 18.sp,
                        color = data.invertedColor,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterVertically)
                    )

                    if (data.id == selectedBillTypeId) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = data.invertedColor,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 8.dp)
                        )
                    }
                }
            }
            if (deleteButtonState.value && selectedBillTypeId.isNotBlank()) {
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Red)
                    .padding(4.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onDeleteButtonClick(data) }) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Delete,
                        tint = Color.White,
                        contentDescription = "delete button",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
@Preview
fun BillTypePreview() {
    BillTypeItem(data = BillTypeData(name = ""),
        modifier = Modifier.height(65.dp),
        onBillTypeSelected = {},
        onNameChanged = {},
        onDeleteButtonClick = {})
}