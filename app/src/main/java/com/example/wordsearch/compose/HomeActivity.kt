package com.example.wordsearch.compose

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.wordsearch.data.DataMuseResult
import com.example.wordsearch.model.WordItem

const val EN_WIKI : String = "enwiki"
const val ES_VOCAB : String = "es"

class HomeActivity : AppCompatActivity(), DataMuseResult {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val provider : WordProvider = WordProvider()
//        provider.fetchRelatedWords(this, "car", 10, EN_WIKI)
//        provider.fetchLeadingWords(this, "wreak", 10, EN_WIKI)
//        provider.fetchTrailingWords(this, "havoc", 10, EN_WIKI)
//        provider.fetchSynonymousWords(this, "bright", 10, EN_WIKI)
        setContent {
            MaterialTheme {
                MessageCard("Nicolas")
            }
        }
    }

    override fun onDataFetchedSuccess(words: List<WordItem>) {
        Log.d("HomeActivity", "fetching  was successful" )
//        TODO : implement logic when words are fetched successfully
    }

    override fun onDataFetchedFailed() {
        Log.d("HomeActivity", "fetching failed" )
        //        TODO : implement when logic the fetch query has failed
    }
}

@Composable
fun MessageCard(s : String){
    Text("hello $s")
}
