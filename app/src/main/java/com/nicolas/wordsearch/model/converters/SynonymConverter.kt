package com.nicolas.wordsearch.model.converters

import androidx.room.TypeConverter
import com.nicolas.wordsearch.data.Synonymous
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class SynonymConverter {
    @TypeConverter
    fun fromSynonymToType(value: List<Synonymous?>): String = Json.encodeToString(value)

    @TypeConverter
    fun toSynonymType(value: String): List<Synonymous?> = Json.decodeFromString(value)
}