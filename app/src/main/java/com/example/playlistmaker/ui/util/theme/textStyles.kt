package com.example.playlistmaker.ui.util.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.playlistmaker.R

val YSDisplayMedium = FontFamily(
    Font(R.font.ys_display_medium)
)

val YSDisplayRegular = FontFamily(
    Font(R.font.ys_display_regular)
)

object AppTextStyles {
    val funcButtonText = TextStyle(
        fontSize = AppDimens.textSize14,
        fontFamily = YSDisplayMedium,
        fontWeight = FontWeight.Medium
    )

    val mediumText = TextStyle(
        fontSize = AppDimens.primaryTextSize,
        fontFamily = YSDisplayMedium,
        fontWeight = FontWeight.Medium
    )

    val searchPlaceholder = TextStyle(
        fontSize = AppDimens.textSize19,
        fontFamily = YSDisplayMedium,
        fontWeight = FontWeight.Normal
    )

    val regularText = TextStyle(
        fontSize = AppDimens.minorTextSize,
        fontFamily = YSDisplayRegular,
        fontWeight = FontWeight.Normal
    )

    val miniRegularText = TextStyle(
        fontSize = AppDimens.miniTextSize,
        fontFamily = YSDisplayRegular,
        fontWeight = FontWeight.Normal
    )
}