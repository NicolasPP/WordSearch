package com.nicolas.wordsearch.model.converters


import androidx.room.TypeConverter
import com.nicolas.wordsearch.data.Leading
import com.nicolas.wordsearch.data.Synonymous
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class LeadingConverter {
    @TypeConverter
    fun fromLeadingToType(value: List<Leading?>): String = Json.encodeToString(value)

    @TypeConverter
    fun toLeadingType(value: String): List<Leading?> = Json.decodeFromString(value)
}