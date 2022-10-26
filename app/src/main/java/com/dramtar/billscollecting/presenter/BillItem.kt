package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.utils.getHourMinute

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun BillItem(
    data: BillData,
    onItemClick: (BillData) -> Unit,
    onDeleteButtonClick: (BillData) -> Unit
) {
    val deleteButtonState = remember { mutableStateOf(false) }

    Box() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .combinedClickable(
                    onClick = {
                        if (!deleteButtonState.value) onItemClick(data)
                        deleteButtonState.value = false
                    },
                    onLongClick = { deleteButtonState.value = true }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BillTypeItem(
                data = data.billTypeData,
                selectedBillTypeId = "",
                onBillTypeSelected = {},
                modifier = Modifier.weight(.5f),
                onNameChanged = {},
                onDeleteButtonClick = {}
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = data.date.getHourMinute(),
                fontSize = 16.sp,
                modifier = Modifier.weight(.2f),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = data.formattedAmount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(.3f),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (deleteButtonState.value) {
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .padding(4.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onDeleteButtonClick(data) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = Color.White,
                    contentDescription = "delete button",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
@Preview
fun BillItemPreview() {
    BillItem(
        data = BillData(),
        onItemClick = {},
        onDeleteButtonClick = {}
    )
}