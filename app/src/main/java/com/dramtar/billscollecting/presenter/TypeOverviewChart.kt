package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TypeOverviewChart(
    fmtDate: String,
    fmtSum: String,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = fmtSum)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = fmtDate)
    }

}