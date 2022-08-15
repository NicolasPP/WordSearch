package com.example.wordsearch.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AddSettingsScreen(
    isDarkTheme: MutableState<Boolean>
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primaryVariant
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            val currentTheme =  if (isDarkTheme.value) "Dark" else "Light"
            Text(
                text = "Theme :  $currentTheme",
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Divider(Modifier.width(10.dp).background(MaterialTheme.colors.primaryVariant))
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
}