package com.example.wordsearch.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wordsearch.compose.themes.colorDarkPalette
import com.example.wordsearch.compose.themes.colorLightPalette

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
            MaterialTheme(
                colors = if (isDarkThemeVal) colorDarkPalette else colorLightPalette
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ){
                    InputText()
                }
            }
        }
    }
}

@Composable
fun InputText(){
    Text(text = "Test", color = MaterialTheme.colors.primary)
}