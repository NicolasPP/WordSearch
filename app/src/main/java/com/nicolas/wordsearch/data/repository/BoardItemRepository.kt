package com.nicolas.wordsearch.data.repository


import androidx.lifecycle.LiveData
import com.nicolas.wordsearch.data.BoardItem


class BoardItemRepository(private val boardItemDao: BoardItemDao) {

    val allItems: LiveData<List<BoardItem>> = boardItemDao.getItems()

    fun insertItem(item: BoardItem) {
        AppDatabase.databaseWriteExecutor.execute {
            boardItemDao.insertItem(item)
        }
    }

    fun insertItems(items: List<BoardItem>) {
        AppDatabase.databaseWriteExecutor.execute {
            boardItemDao.insertItems(items)
        }
    }
}