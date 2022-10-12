package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
                    "Total amount ${viewModel.billListState.formattedTotalSum}",
                    fontSize = 28.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(CenterHorizontally),
                )

                viewModel.billListState.overviewTypesList?.let { list ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                    ) {
                        items(items = list) { chartItem ->
                            AmountChartItem(
                                data = chartItem.type,
                                amount = chartItem.formattedSumAmount,
                                progress = chartItem.percentage,
                                formattedPercentage = chartItem.formattedPercentage
                            )
                        }
                    }
                }
            }
        }
    }
}