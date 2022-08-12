package com.example.wordsearch.data

import com.example.wordsearch.model.WordItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WordApi {
    @GET("words")
    fun fetchWords(@Query(value="ml")keyword:String) : Call<List<WordItem>>
}