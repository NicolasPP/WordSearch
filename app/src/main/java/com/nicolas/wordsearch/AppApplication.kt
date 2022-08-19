package com.nicolas.wordsearch

import android.app.Application
import com.nicolas.wordsearch.data.repository.AppDatabase
import com.nicolas.wordsearch.data.repository.BoardItemRepository

class AppApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { BoardItemRepository(database.boardDao()) }
}