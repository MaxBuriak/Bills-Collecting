package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TypeOverviewChart(
    chartData: TypeChartData,
    typeData: TypeOverviewData
) {
    Column(Modifier.padding(8.dp).clickable {

    }) {
        Text(text = chartData.formattedSum)
        Spacer(modifier = Modifier.height(8.dp))
        VerticalProgress(
            modifier = Modifier
                .weight(.3F)
                .align(CenterHorizontally),
            progress = chartData.percentage,
            color = typeData.type.color,
            invertedColor = typeData.type.invertedColor
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = chartData.date)
    }

}