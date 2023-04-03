package com.dramtar.billscollecting.presenter.type_overview.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VerticalProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color,
    invertedColor: Color
) {
    val mProgress = animateFloatAsState(targetValue = progress / 100)
    Column(
        modifier = modifier
            .width(24.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(if ((1 - mProgress.value) == 0F) 0.0001F else 1 - mProgress.value)
                .fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
                .weight(mProgress.value)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(color, invertedColor))
                )
        )
    }
}