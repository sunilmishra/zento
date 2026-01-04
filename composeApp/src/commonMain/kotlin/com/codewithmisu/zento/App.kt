package com.codewithmisu.zento

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.codewithmisu.zento.theme.zDarkColorScheme
import com.codewithmisu.zento.theme.zLightColorScheme

@Composable
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) zDarkColorScheme else zLightColorScheme,
    ) {
        AppNavigation()
    }
}