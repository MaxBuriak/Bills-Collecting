package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dramtar.billscollecting.domain.BillTypeData

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun AmountChartItem(
    data: BillTypeData,
    amount: String,
    progress: Float,
    formattedPercentage: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        BillTypeItem(
            data = data,
            onBillTypeSelected = {},
            onNameChanged = {},
            onDeleteButtonClick = {},
            modifier = Modifier.weight(.3F)
        )
        Text(
            text = amount,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(.2F),
        )
        LinearProgressIndicator(
            modifier = Modifier
                .height(15.dp)
                .weight(.3F),
            progress = progress,
            color = data.color,
        )
        Text(
            text = formattedPercentage,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(.1F),
        )
    }
}