package com.dramtar.billscollecting.presenter.bills.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DateStickyHeader(
    modifier: Modifier = Modifier,
    formattedDate: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 6.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 6.dp
            )
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.Center)
        ) {
            Text(
                formattedDate,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 6.dp, end = 6.dp)
            )
        }
    }
}