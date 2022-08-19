package com.nicolas.wordsearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nicolas.wordsearch.data.WordProvider
import com.nicolas.wordsearch.data.DataMuseResult
import com.nicolas.wordsearch.model.WordItem

const val TAG = "DataMuseViewModel"

class DataMuseViewModel : ViewModel(), DataMuseResult {

    private val _wordItems = MutableLiveData<List<WordItem>>()
    val wordItems : LiveData<List<WordItem>> = _wordItems

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val provider by lazy{
        WordProvider()
    }


    fun getRelatedWords(
        dataResult : DataMuseResult,
        inputString : String,
        max : Int,
        vocab : String
        ){
        provider.fetchRelatedWords(dataResult, inputString, max,  vocab)
    }

    fun getTrailingWords(
        dataResult : DataMuseResult,
        inputString : String,
        max : Int,
        vocab : String,
        currentWordIndex : Int
    ){
        provider.fetchTrailingWords(dataResult, inputString, max,  vocab, currentWordIndex)
    }

    fun getLeadingWords(
        dataResult : DataMuseResult,
        inputString : String,
        max : Int,
        vocab : String,
        currentWordIndex : Int
    ){
        provider.fetchLeadingWords(dataResult, inputString, max,  vocab, currentWordIndex)
    }

    fun getSynonymousWords(
        dataResult : DataMuseResult,
        inputString : String,
        max : Int,
        vocab : String
    ){
        provider.fetchSynonymousWords(dataResult, inputString, max,  vocab)
    }


    override fun onDataFetchedSuccess(words: List<WordItem>) {
        Log.d(TAG, "fetching  was successful" )
        _wordItems.value = words
    }

    override fun onDataFetchedFailed() {
        Log.d(TAG, "fetching failed" )
        _error.value = true
    }

}