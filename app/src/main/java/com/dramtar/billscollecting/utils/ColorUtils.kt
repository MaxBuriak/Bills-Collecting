package com.dramtar.billscollecting.utils

import androidx.compose.ui.graphics.Color
import java.util.*

//fun Color.invertColor() = Color(
//    red = 1 - red,
//    green = 1 - green,
//    blue = 1 - blue
//)

fun Color.getOnColor(): Color {
    return if (red > 0.3 && blue > 0.3 && green > 0.3) {
        Color.DarkGray
    } else {
        Color.White
    }
}

fun Color.makeMoreSolid() = Color(
    red = red - (red / 3F),
    blue = blue - (blue / 3F),
    green = green - (green / 1.5F),
)

fun Color.Companion.getRndColor() = Color(Random().nextInt()).makeMoreSolid()