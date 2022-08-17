package com.nicolas.wordsearch.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nicolas.wordsearch.gamelogic.GameLetter
import kotlin.math.abs
import kotlin.math.max

class Offset (val x : Int, val y : Int, val size : Int){
    companion object {
        fun getOffset(entry : GameLetter, exit : GameLetter) : Offset{
            val (entryRow, entryCol) = entry
            val (exitRow, exitCol) = exit
            val xDiff = entryRow - exitRow
            val yDiff = entryCol - exitCol
            val value = max(abs(xDiff), abs(yDiff))
            return Offset(getOffsetDiff(xDiff), getOffsetDiff(yDiff), value)
        }
        private fun getOffsetDiff(diff: Int): Int {
            if (diff == 0) return 0
            if (diff < 0) return -1
            return 1
        }
    }

}

class GameManagerViewModel : ViewModel(){
    private val _pickedLetters = MutableLiveData<List<GameLetter>> ()
    private val currentSelection : LiveData<List<GameLetter>> = _pickedLetters

    private val _pickedWord = MutableLiveData<List<GameLetter>> ()
    private val pickedWord : LiveData<List<GameLetter>> = _pickedWord



    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    // select and unselect Letters

    fun addLetter( letter : GameLetter ) {
        if (isSelectionFull())
            invalidatePickedLetters()
        if (letter !in getSelection())
            _pickedLetters.value = getSelection() + letter
    }

    fun removeLetter( letter : GameLetter ) {
        if(!isSelectionEmpty())
            _pickedLetters.value = getSelection() - letter
    }

    fun addWordLetters(){
        val entry = currentSelection.value?.get(0) ?: GameLetter(0,0,"")
        val exit = currentSelection.value?.get(1) ?: GameLetter(0,0,"")
        val offset = Offset.getOffset(entry, exit)
        var (entryRow, entryCol) = entry
        for (i in 0..offset.size){
            _pickedWord.value = getPickedWord() +
                    GameLetter(entryRow, entryCol, "")
            entryRow -= offset.x
            entryCol -= offset.y
        }
    }

    // getters

    fun getPickedWord() : List<GameLetter> {
        return pickedWord.value ?: emptyList<GameLetter>()
    }

    fun getSelection() : List<GameLetter> {
        return currentSelection.value ?: emptyList<GameLetter>()
    }

    // check if picked letters are valid
    // and supporting functions

    fun isSelectionValid() : Boolean {
//        if (isSelectionEmpty()) return false
        if (!isSelectionFull()) return false
        if (!isSelectionAligned()) return false
        return true
    }

    private fun isSelectionFull()  : Boolean {
        if ((currentSelection.value?.size ?: -1) == 2) return true
        return false
    }

    private fun isSelectionEmpty()  : Boolean {
        if ((currentSelection.value?.size ?: - 1) == 0) return true
        return false
    }

    private fun isSelectionAligned() : Boolean {
        val entryPoint = currentSelection.value?.get(0) ?: GameLetter(-1,-1,"")
        val exitPoint = currentSelection.value?.get(1) ?: GameLetter(-1,-1,"")
        val (entryRow, entryCol) = entryPoint
        val (exitRow, exitCol) = exitPoint
        if (!isSelectionFull()) return false
        if (entryRow == exitRow) return true
        if (entryCol == exitCol) return true
        if (abs(entryRow - exitRow) == abs(entryCol - exitCol)) return true
        return false
    }

    private fun invalidatePickedLetters() {
        _pickedLetters.value = emptyList<GameLetter>()
    }

    private fun invalidatePickedWord() {
        _pickedWord.value = emptyList<GameLetter>()
    }

}