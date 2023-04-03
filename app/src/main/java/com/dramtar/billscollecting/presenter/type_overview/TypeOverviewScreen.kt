package com.dramtar.billscollecting.presenter.type_overview

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dramtar.billscollecting.presenter.overview.components.TypeOverviewChart
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme

@Composable
fun TypeOverviewScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: TypeOverviewViewModel = hiltViewModel()
) {
    val typeOverviewState = viewModel.typeOverviewState
    BillsCollectingTheme {
        val gradientColor = Brush.verticalGradient(
            0.2f to typeOverviewState.type.color,
            1f to typeOverviewState.type.invertedColor
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(brush = gradientColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(34.dp))
            Text(
                text = typeOverviewState.type.name,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = typeOverviewState.type.invertedColor
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = typeOverviewState.fmtPeriodOfTime,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = typeOverviewState.type.invertedColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = typeOverviewState.fmtSumTotal,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = typeOverviewState.type.invertedColor
            )
            Spacer(modifier = Modifier.height(6.dp))
/*            Text(
                text = typeOverviewState.fmtCurrPeriodOfTime,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                color = typeOverviewState.type.invertedColor
            )
            Spacer(modifier = Modifier.height(8.dp))*/
            /*Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = typeOverviewState.fmtSumCurrAmount,
                    modifier = Modifier.weight(1F),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = typeOverviewState.type.invertedColor
                )*/
/*                Text(
                    text = stringResource(
                        id = R.string.percent_placeholder,
                        typeOverviewState.fmtCurrMonthPercentage
                    ),
                    modifier = Modifier
                        .weight(1F)
                        .align(Alignment.CenterVertically),
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = typeOverviewState.type.invertedColor
                )
            }*/
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
                typeOverviewState.gpdByDate?.forEach { chartData ->
                    TypeOverviewChart(
                        typeData = typeOverviewState,
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
        navController = rememberNavController()
    )
}