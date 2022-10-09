package com.dramtar.billscollecting.presenter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme

@Composable
fun OverviewScreen() {
    BillsCollectingTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    "Overview",
                    fontSize = 32.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}