package com.nicolas.wordsearch.compose.utils

import androidx.appcompat.app.AppCompatDelegate


fun isDarkThemeEnabled(): Boolean {
    return AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO
}