package com.example.wordsearch.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.wordsearch.data.WordProvider

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val provider : WordProvider = WordProvider()
        provider.fetchWord("android")



        setContent {

            MaterialTheme {
                MessageCard("Nicolas")
            }
        }
    }
}

@Composable
fun MessageCard(s : String){
    Text("hello $s")
}
