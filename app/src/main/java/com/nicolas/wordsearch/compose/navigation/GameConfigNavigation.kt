package com.nicolas.wordsearch.compose.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nicolas.wordsearch.R

sealed class GameConfigNavigation (
    val route: String,
    @StringRes val stringResId : Int,
    @DrawableRes val drawResId : Int
) {
    object Play : HomeNavigation("Play", R.string.LeaderBoard, R.drawable.play_arrow_48px)
    object Return : HomeNavigation("Return", R.string.Settings, R.drawable.outline_arrow_back_24)
}