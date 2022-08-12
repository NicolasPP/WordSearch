package com.example.wordsearch.data

import com.example.wordsearch.model.WordItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WordApi {
    @GET("words")
    fun fetchSynWords(
        @Query(value="rel_syn")relSyn:String,
        @Query(value="max") max : String,
        @Query(value="v") vocabulary : String)
    : Call<List<WordItem>>
    @GET("words")
    fun fetchTrailingWords(
        @Query(value="rel_bgb")relBgb:String,
        @Query(value="max") max : String,
        @Query(value="v") vocabulary : String)
            : Call<List<WordItem>>
    @GET("words")
    fun fetchLeadingWords(
        @Query(value="rel_bga")relBga:String,
        @Query(value="max") max : String,
        @Query(value="v") vocabulary : String)
            : Call<List<WordItem>>
    @GET("words")
    fun fetchRelatedWords(
        @Query(value="rel_gen")relGen:String,
        @Query(value="max") max : String,
        @Query(value="v") vocabulary : String)
    : Call<List<WordItem>>
}