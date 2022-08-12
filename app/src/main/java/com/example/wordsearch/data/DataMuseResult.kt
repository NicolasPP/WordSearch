package com.example.wordsearch.data

import com.example.wordsearch.model.WordItem

interface DataMuseResult {
    fun onDataFetchedSuccess(words : List<WordItem>)
    fun onDataFetchedFailed()
}