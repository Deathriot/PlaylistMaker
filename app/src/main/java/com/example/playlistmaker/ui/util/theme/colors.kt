package com.example.playlistmaker.ui.util.theme

import androidx.compose.ui.graphics.Color

class AppColors(
    val colorSecondary: Color,
    val colorOnSecondary: Color,
    val black: Color,
    val white: Color,
    val blue: Color,
    val gray: Color,
    val lightBlue: Color,
    val lightGray: Color,
    val vectorColor: Color,
    val grayToWhite: Color,
    val lightGrayToWhite: Color,
)

val LightAppColors = AppColors(
    colorSecondary = Color(0xFFFFFFFF),
    colorOnSecondary = Color(0xFF1A1B22),
    black = Color(0xFF1A1B22),
    white = Color(0xFFFFFFFF),
    blue = Color(0xFF3772E7),
    gray = Color(0xFFAEAFB4),
    lightBlue = Color(0xFF9FBBF3),
    lightGray = Color(0xFFE6E8EB),
    vectorColor = Color(0xFFAEAFB4),
    grayToWhite = Color(0xFFAEAFB4),
    lightGrayToWhite = Color(0xFFE6E8EB)
)

val DarkAppColors = AppColors(
    colorSecondary = Color(0xFF1A1B22),
    colorOnSecondary = Color(0xFFFFFFFF),
    black = Color(0xFF1A1B22),
    white = Color(0xFFFFFFFF),
    blue = Color(0xFF3772E7),
    gray = Color(0xFFAEAFB4),
    lightBlue = Color(0xFF9FBBF3),
    lightGray = Color(0xFFE6E8EB),
    vectorColor = Color(0xFF1A1B22),
    grayToWhite = Color(0xFFFFFFFF),
    lightGrayToWhite = Color(0xFFFFFFFF)
)