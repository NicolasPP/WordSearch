package com.nicolas.wordsearch.compose.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nicolas.wordsearch.R

sealed class HomeNavigation(
    val route: String,
    @StringRes val stringResId : Int,
    @DrawableRes val drawResId : Int
) {
    object LeaderBoard : HomeNavigation("LeaderBoard", R.string.LeaderBoard, R.drawable.trophy_icon_24)
    object Home : HomeNavigation("Home", R.string.Settings, R.drawable.home_icon_24)
}