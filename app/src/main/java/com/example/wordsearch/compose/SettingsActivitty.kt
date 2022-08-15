package com.example.wordsearch.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddSettingsScreen(
    isDarkTheme: MutableState<Boolean>
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Switch(
            checked = isDarkTheme.value,
            onCheckedChange = {
                isDarkTheme.value = it

            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.DarkGray,
                uncheckedThumbColor = Color.DarkGray,
                checkedTrackColor = Color.Blue,
                uncheckedTrackColor = Color.Blue,
            )
        )
    }
}