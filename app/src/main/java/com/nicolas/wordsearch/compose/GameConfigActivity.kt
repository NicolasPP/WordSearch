package com.nicolas.wordsearch.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicolas.wordsearch.R
import com.nicolas.wordsearch.compose.themes.colorDarkPalette
import com.nicolas.wordsearch.compose.themes.colorLightPalette


class GameConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isDarkThemeVal = if (intent.extras?.containsKey("isDarkThemeVal") == true) {
            intent.extras!!.getBoolean("isDarkThemeVal", false)
        } else {
            finish()
            return
        }
        setContent{
            AddContent(
                isDarkThemeVal = isDarkThemeVal
            )
        }
    }
}

@Composable
fun AddContent(
    isDarkThemeVal : Boolean
){

    MaterialTheme(
        colors = if (isDarkThemeVal) colorDarkPalette else colorLightPalette
    ){
        Scaffold(
            bottomBar = { AddBottomAppBar()},
            floatingActionButton = {
                AddFloatingAction(
                    iconId = R.drawable.play_arrow_48px,
                    dpSize = 56,
                    bgColor = MaterialTheme.colors.primary,
                    fgColor = MaterialTheme.colors.background,
                    cDescription = "play",
                    modifier = Modifier
                ){
                }},
            topBar = { AddTopAppBar(isDarkTheme = isDarkThemeVal) },
            floatingActionButtonPosition = FabPosition.End,
            isFloatingActionButtonDocked = true,
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = it.calculateBottomPadding())
            ){
                AddScaffoldContent()
            }
        }

    }
}

@Composable
fun AddBottomAppBar(){
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.background),
        cutoutShape = MaterialTheme.shapes.small.copy(
            CornerSize(percent = 50)
        ),
        backgroundColor = MaterialTheme.colors.onBackground
    ){

    }
}

@Composable
fun AddScaffoldContent(){
    Divider(
        Modifier
            .background(MaterialTheme.colors.background)
            .height(40.dp), color = MaterialTheme.colors.background)

    AddThemeInputField()

    Divider(
        Modifier
            .background(MaterialTheme.colors.background)
            .height(40.dp), color = MaterialTheme.colors.background)

    AddMultiButtonSingleChoice()

}

@Composable
fun AddMultiButtonSingleChoice() {

}

@Composable
fun AddThemeInputField(){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colors.background)
    ) {
        var text = remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            modifier = Modifier
                .clip(RoundedCornerShape(60.dp)),
            value = text.value.text,
            onValueChange = { newText: String ->
                text.value = TextFieldValue(newText)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                textColor = MaterialTheme.colors.primary
            ),
            label = { Text(
                text = "Enter a Theme",
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 40.dp)
            ) },
            placeholder = { Text(
                text = "an adjective works better for synonyms",
                color = MaterialTheme.colors.onBackground,
                fontSize = 12.sp
            )}
        )
    }
}

@Composable
fun AddTopAppBar(
    isDarkTheme : Boolean
){
    val context = LocalContext.current
    TopAppBar(
        backgroundColor  = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(30.dp))
            .background(MaterialTheme.colors.onBackground)
            .padding(start = 8.dp, end = 8.dp)
    ){
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 10.dp)
        ){
            AddFloatingAction(
                iconId = R.drawable.outline_arrow_back_24,
                dpSize = 50,
                bgColor = MaterialTheme.colors.primary,
                fgColor = MaterialTheme.colors.background,
                modifier = Modifier
                    .size(40.dp),
                cDescription = "Settings"){
                val intent = Intent(context, HomeActivity::class.java)
                intent.putExtra("isDarkThemeVal", isDarkTheme)
                context.startActivity(intent)
            }
        }
    }
}


