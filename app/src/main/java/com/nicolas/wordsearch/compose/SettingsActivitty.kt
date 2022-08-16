package com.nicolas.wordsearch.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicolas.wordsearch.R


@Composable
fun AddSettingsScreen(
    isDarkTheme: MutableState<Boolean>,
    settingIconPos: MutableState<Float>
    ){
    val loggedIn = remember { mutableStateOf(false) }
    val size = with (LocalDensity.current){ settingIconPos.value.toDp()}

    Column(
        modifier = Modifier
            .width(size)
            .fillMaxHeight()
            .background(
                MaterialTheme.colors.primaryVariant
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AddThemeSwitch(
            isDarkTheme
        )
        AddLogInButton( R.drawable.person_48px)
        AddLogInInputs("UserName")
        AddLogInButton( R.drawable.outline_arrow_forward_24)
    }
}

@Composable
fun AddLogInButton(
    iconId : Int
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            modifier = Modifier,
            onClick = {}
        ){
            Image(
                painter = painterResource(id = iconId),
                contentDescription = R.string.LogIn.toString(),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
        }
    }
}

@Composable
fun AddLogInInputs(
    inputLabel : String
){
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var text = remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = text.value.text,
            onValueChange = { newText: String ->
                text.value = TextFieldValue(newText)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                textColor = MaterialTheme.colors.primary
            ),
            label = { Text(
                text = inputLabel,
                color = MaterialTheme.colors.primary,
                fontSize = 17.sp
            )},
        )
    }
}



@Composable
fun AddThemeSwitch(
    isDarkTheme: MutableState<Boolean>
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
        Divider(
            Modifier
                .width(10.dp)
                .background(MaterialTheme.colors.primaryVariant))
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

