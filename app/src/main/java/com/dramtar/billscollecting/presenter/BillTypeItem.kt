package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
    selectedBillType: BillTypeData? = null,
    onBillTypeSelected: (data: BillTypeData) -> Unit,
    onNameChanged: (name: String) -> Unit,
    onDeleteButtonClick: (BillTypeData) -> Unit,
) {
    val deleteButtonState = remember { mutableStateOf(false) }
    val nameInputValue = remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(data.color)
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 3.dp)
                .selectable(
                    selected = selectedBillType?.id == data.id,
                    onClick = {},
                )
                .combinedClickable(
                    onClick = {
                        if (!deleteButtonState.value) onBillTypeSelected(data)
                        deleteButtonState.value = false
                    }, onLongClick = { deleteButtonState.value = true }
                )
        ) {
            if (data.name.isBlank()) {
                val focusRequester = remember { FocusRequester() }
                BasicTextField(
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
                        .padding(8.dp)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        color = data.invertedColor,
                        fontSize = 18.sp
                    ),
                    cursorBrush = Brush.verticalGradient(
                        0.00f to data.invertedColor,
                        0.35f to data.invertedColor,
                        0.35f to data.invertedColor,
                        0.90f to data.invertedColor,
                        0.90f to data.invertedColor,
                        1.00f to data.invertedColor,
                    ),
                    singleLine = true,
                    maxLines = 1,
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            } else {
                Text(
                    text = data.name,
                    fontSize = 18.sp,
                    color = data.invertedColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )

                if (data.id == selectedBillType?.id) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        tint = data.invertedColor,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }
        if (deleteButtonState.value && selectedBillType != null) {
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

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
@Preview
fun BillTypePreview() {
    BillTypeItem(data = BillTypeData(
        name = "kekwwwwwwwwwwwww",
        id = "kekwwwwwwwwwwwww"
    ),
        selectedBillType = BillTypeData(),
        modifier = Modifier.height(73.dp),
        onBillTypeSelected = {},
        onNameChanged = {},
        onDeleteButtonClick = {})
}