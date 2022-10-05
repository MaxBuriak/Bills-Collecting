package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dramtar.billscollecting.domain.BillData
import com.dramtar.billscollecting.utils.getHourMinute

@ExperimentalComposeUiApi
@Composable
fun BillItem(item: BillData, onItemClick: (BillData) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        BillTypeItem(
            billType = item.billTypeData,
            selectedBillTypeId = "",
            onBillTypeSelected = {},
            modifier = Modifier.weight(1f),
            onNameChanged = {}
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = item.date.getHourMinute(),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = item.formattedAmount,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}