package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dramtar.billscollecting.R
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun OverviewScreen(
    navController: NavController,
    viewModel: MainViewModel,
    onExportCLicked: () -> Unit
) {
    BillsCollectingTheme {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(id = R.string.overview_title),
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(CenterHorizontally),
                )

                Divider(
                    color = MaterialTheme.colorScheme.onSecondary,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = stringResource(id = R.string.export_title),
                    fontSize = 28.sp,
                    modifier = Modifier
                        .padding(12.dp)
                        .align(CenterHorizontally)
                        .clickable { onExportCLicked() },
                )
                Divider(
                    color = MaterialTheme.colorScheme.onSecondary,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Text(
                    text = stringResource(
                        id = R.string.total_sum_title,
                        viewModel.billListState.formattedTotalSum
                    ),
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