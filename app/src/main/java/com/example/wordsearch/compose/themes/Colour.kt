package com.example.wordsearch.compose.themes

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

val white = Color.White
val black = Color.Black
val transparent = Color.Transparent


// light pallet exclusive
val darkGray = Color.DarkGray
val donBrown = Color(0xFFA39081) // darkest
val tan = Color(0xFFD6A692)
val chamois = Color(0xFFEFD9b4)
val opal = Color(0xFFB1C8C7)
val beige = Color(0xFFF5F4DE) // lightest


// dark pallet exclusive
val lightGray = Color.LightGray
val darkBlue1 = Color(0xFF121415) // darkest
val darkBlue2 = Color(0xFF172227)
val darkBlue3 = Color(0xFF233540)
val blue = Color(0xFF26415A)
val lighterBlue = Color(0xFF417194) // lightest


val colorDarkPalette = darkColors(
    primary = lighterBlue, // contrast with main background color
    primaryVariant = black,
    secondary = darkGray, // unselected color
    secondaryVariant = black,
    background = darkBlue2, // Main background color
    surface = transparent,
    error  = black,
    onPrimary = black,
    onSecondary = black,
    onBackground = darkBlue1, // Secondary background color - darker
    onSurface = black,
    onError = black,
)

val colorLightPalette = darkColors(
    primary = tan,// contrast with main background color
    primaryVariant = white,
    secondary = lightGray, // unselected color
    secondaryVariant = white,
    background = opal, // Main background color
    surface = transparent,
    error  = white,
    onPrimary = white,
    onSecondary = white,
    onBackground = donBrown, // Secondary background color - darker
    onSurface = white,
    onError = white
)
