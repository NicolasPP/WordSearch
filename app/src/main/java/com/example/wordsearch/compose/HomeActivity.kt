package com.example.wordsearch.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

const val EN_WIKI : String = "enwiki"
const val ES_VOCAB : String = "es"

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
