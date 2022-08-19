package com.nicolas.wordsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.nicolas.wordsearch.model.converters.LeadingConverter
import com.nicolas.wordsearch.model.converters.SynonymConverter
import kotlinx.serialization.Serializable
@Serializable
@Entity(tableName = "BoardItem")
data class BoardItem (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @field:TypeConverters(LeadingConverter::class)
    val leading: List<Leading>,
    @field:TypeConverters(SynonymConverter::class)
    val synonymous: List<Synonymous>
)