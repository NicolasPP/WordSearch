package com.nicolas.wordsearch.data

import kotlinx.serialization.Serializable

@Serializable
data class Leading(
   // val Board: List<List<String>>,
    val Input: String,
    val Size: Int,
    //val Words: List<String>
)