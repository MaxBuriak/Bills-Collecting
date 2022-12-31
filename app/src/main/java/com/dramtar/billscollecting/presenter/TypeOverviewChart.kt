package com.dramtar.billscollecting.presenter

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
    typeData: TypeOverviewData,
    modifier: Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        VerticalProgress(
            modifier = Modifier
                .weight(.3F)
                .align(CenterHorizontally),
            progress = chartData.percentage,
            color = typeData.type.color,
            invertedColor = typeData.type.invertedColor
        )
        //Text(text = chartData.formattedSum)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = chartData.date)
    }

}