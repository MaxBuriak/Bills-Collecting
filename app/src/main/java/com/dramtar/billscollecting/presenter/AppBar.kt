package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dramtar.billscollecting.utils.getMonthYear
import java.text.NumberFormat

@Composable
fun AppBar(
    onNavigationClick: () -> Unit,
    totalSum: Int,
    onDateClick: () -> Unit,
    selectedRangeDate: Long
) {

    val sumFormatted = remember {
        mutableStateOf("")
    }
    sumFormatted.value = NumberFormat.getCurrencyInstance().format(totalSum)
    TopAppBar(
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDateClick()
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Total sum ${sumFormatted.value}",
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
        },
        /*backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,*/
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.primary
    )
}