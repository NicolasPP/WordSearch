package com.nicolas.wordsearch.data

import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class Synonymous(
    val board: List<String>,
    val input: String,
    val size: Int,
    val words: List<String>
)