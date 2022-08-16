package com.nicolas.wordsearch.data

import com.nicolas.wordsearch.model.WordItem

interface DataMuseResult {
    fun onDataFetchedSuccess(words : List<WordItem>)
    fun onDataFetchedFailed()
}