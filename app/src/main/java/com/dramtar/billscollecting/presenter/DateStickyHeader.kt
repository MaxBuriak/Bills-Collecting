package com.dramtar.billscollecting.presenter

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
                top = 16.dp,
                start = 8.dp,
                end = 16.dp,
                bottom = 8.dp
            )
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colorScheme.tertiary)
                .align(Alignment.Center)
        ) {
            Text(
                formattedDate,
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(6.dp)
            )
        }
    }
}