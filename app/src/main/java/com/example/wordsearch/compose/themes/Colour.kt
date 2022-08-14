package com.example.wordsearch.compose.themes

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

val white = Color.White
val black = Color.Black
val lightGray = Color.LightGray
val darkGray = Color.DarkGray
val transparent = Color.Transparent



val colorDarkPalette = darkColors(
    primary = black,
    primaryVariant = black,
    secondary = darkGray,
    secondaryVariant = black,
    background = black,
    surface = transparent,
    error  = black,
    onPrimary = white,
    onSecondary = white,
    onBackground = white,
    onSurface = white,
    onError = white,
)

val colorLightPalette = darkColors(
    primary = white,
    primaryVariant = white,
    secondary = lightGray,
    secondaryVariant = white,
    background = white,
    surface = transparent,
    error  = white,
    onPrimary = black,
    onSecondary = black,
    onBackground = black,
    onSurface = black,
    onError = black
)
