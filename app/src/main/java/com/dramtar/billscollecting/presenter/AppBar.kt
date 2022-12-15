package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dramtar.billscollecting.R
import com.dramtar.billscollecting.utils.getMonthYear

@Composable
fun AppBar(
    onNavigationClick: () -> Unit,
    totalSum: String,
    onDateClick: () -> Unit,
    onChartsClick: () -> Unit,
    onRedChartsClick: () -> Unit,
    selectedRangeDate: Long
) {
    TopAppBar(
        title = {
            Box {
                Button(
                    onClick = onRedChartsClick,
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chart_outlined),
                        contentDescription = "All charts"
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center).padding(start = 55.dp, end = 55.dp)
                        .clickable {
                            onDateClick()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(id = R.string.total_sum_title, totalSum),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = selectedRangeDate.getMonthYear(),
                        fontSize = 18.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Button(
                    onClick = onChartsClick,
                    modifier = Modifier.align(Alignment.CenterEnd),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bar_chart),
                        contentDescription = "charts"
                    )
                }
            }
        },
        /*backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,*/
//        navigationIcon = {
//            IconButton(onClick = onNavigationClick) {
//                Icon(
//                    imageVector = Icons.Default.Menu,
//                    contentDescription = "Toggle drawer",
//                    tint = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//        },
        backgroundColor = MaterialTheme.colorScheme.primary
    )
}