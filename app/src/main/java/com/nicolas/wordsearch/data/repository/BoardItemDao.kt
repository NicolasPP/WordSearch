package com.nicolas.wordsearch.data.repository


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nicolas.wordsearch.data.BoardItem

@Dao
interface BoardItemDao {

    @Query("SELECT * FROM BoardItem")
    fun getItems(): LiveData<List<BoardItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItem(item: BoardItem)

    @Insert
    fun insertItems(items: List<BoardItem>)
}
