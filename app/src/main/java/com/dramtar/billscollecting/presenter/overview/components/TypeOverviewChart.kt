package com.dramtar.billscollecting.presenter.overview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dramtar.billscollecting.presenter.type_overview.components.VerticalProgress
import com.dramtar.billscollecting.presenter.type_overview.TypeChartData
import com.dramtar.billscollecting.presenter.type_overview.TypeOverviewState

@Composable
fun TypeOverviewChart(
    chartData: TypeChartData,
    typeData: TypeOverviewState
) {
    Column(Modifier.padding(7.dp)) {
        Text(text = chartData.formattedSum)
        Spacer(modifier = Modifier.height(8.dp))
        VerticalProgress(
            modifier = Modifier.weight(.3F).align(CenterHorizontally),
            progress = chartData.percentage,
            color = typeData.type.color,
            invertedColor = typeData.type.invertedColor
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = chartData.date,
            maxLines = 2,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(CenterHorizontally).width(38.dp)
        )
    }
}

@Composable
@Preview
fun TypeOverviewChartPreview() {
    TypeOverviewChart(
        chartData = TypeChartData(
            date = "Aug 2023",
            sum = 100.0,
            formattedSum = "100",
            percentage = 30F,
            formattedPercentage = "30%"
        ),
        typeData = TypeOverviewState()
    )
}