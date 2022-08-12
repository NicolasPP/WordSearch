package com.example.wordsearch.compose.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.wordsearch.R

sealed class BottomNavigationScreen(
    val route: String,
    @StringRes val stringResId : Int,
    @DrawableRes val drawResId : Int
) {
    object Home : BottomNavigationScreen("Home", R.string.Home, R.drawable.home_icon_24)
    object LeaderBoard : BottomNavigationScreen("LeaderBoard", R.string.LeaderBoard, R.drawable.trophy_icon_24)
    object Settings : BottomNavigationScreen("Settings", R.string.Settings, R.drawable.settings_icon_24)
}