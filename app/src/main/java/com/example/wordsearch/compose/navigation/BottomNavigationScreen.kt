package com.example.wordsearch.compose.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.wordsearch.R

sealed class BottomNavigationScreen(
    val route: String,
    @StringRes val stringResId : Int,
    @DrawableRes val drawResId : Int
) {
    object LeaderBoard : BottomNavigationScreen("LeaderBoard", R.string.LeaderBoard, R.drawable.trophy_icon_24)
    object Home : BottomNavigationScreen("Home", R.string.Settings, R.drawable.home_icon_24)
}