package com.example.playlistmaker.ui.util.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppColors =
    staticCompositionLocalOf<AppColors> { throw IllegalStateException("Ошибка с цветами") }

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) DarkAppColors else LightAppColors

    CompositionLocalProvider(LocalAppColors provides colors) {
        content()
    }
}

// нашел прикольную фичу - можно использовать без подводных?
@Composable
fun colors(): AppColors = LocalAppColors.current