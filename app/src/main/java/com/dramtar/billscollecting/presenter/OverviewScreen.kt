package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dramtar.billscollecting.domain.BillTypeData
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun OverviewScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    BillsCollectingTheme {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    "Overview",
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(CenterHorizontally),
                )
                Text(
                    "Total amount ${viewModel.billListState.totalSum}",
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(CenterHorizontally),
                )
                viewModel.billListState.overviewTypesList?.forEach {
                    AmountChart(
                        data = it.type,
                        amount = it.formattedSumAmount,
                        progress = it.percentage,
                        formattedPercentage = it.formattedPercentage
                    )
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun AmountChart(
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
                .weight(.4F),
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