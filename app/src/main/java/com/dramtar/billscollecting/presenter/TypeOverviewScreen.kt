package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dramtar.billscollecting.R
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme

@Composable
fun TypeOverviewScreen(
    modifier: Modifier,
    navController: NavController?,
    typeOverviewData: TypeOverviewData
) {
    BillsCollectingTheme {
        val gradientColor = Brush.verticalGradient(
            0.2f to typeOverviewData.type.color,
            1f to typeOverviewData.type.invertedColor
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(brush = gradientColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(34.dp))
            Text(
                text = typeOverviewData.type.name,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = typeOverviewData.type.invertedColor
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = typeOverviewData.fmtPeriodOfTime,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = typeOverviewData.type.invertedColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = typeOverviewData.fmtSumTotal,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = typeOverviewData.type.invertedColor
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(id = R.string.curr_month_title),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = typeOverviewData.type.invertedColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = typeOverviewData.fmtSumCurrAmount,
                    modifier = Modifier.weight(1F),
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = typeOverviewData.type.invertedColor
                )
                Text(
                    text = stringResource(
                        id = R.string.percent_placeholder,
                        typeOverviewData.fmtCurrMonthPercentage
                    ),
                    modifier = Modifier
                        .weight(1F)
                        .align(Alignment.CenterVertically),
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = typeOverviewData.type.invertedColor
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(start = 16.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .height(250.dp),
            ) {
                typeOverviewData.gpdByDate?.forEach { chartData ->
                    TypeOverviewChart(
                        typeData = typeOverviewData,
                        chartData = chartData
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
fun TypeOverviewScreenPreview() {
    TypeOverviewScreen(
        modifier = Modifier,
        navController = null,
        typeOverviewData = TypeOverviewData(
            type = BillTypeData(color = Green, invertedColor = White),
            fmtSumTotal = "100$",
            fmtCurrMonthPercentage = "50%",
            fmtSumCurrAmount = "100$"
        )
    )
}