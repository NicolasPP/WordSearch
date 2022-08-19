package com.nicolas.wordsearch.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nicolas.wordsearch.Board
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

class GameManagerViewModel (
    val board : Board
        ) : ViewModel(){


    private val _pickedLetters = MutableLiveData<List<GameLetter>> ()
    private val currentSelection : LiveData<List<GameLetter>> = _pickedLetters

    private val _pickedWord = MutableLiveData<List<GameLetter>> ()
    private val pickedWord : LiveData<List<GameLetter>> = _pickedWord

    private val _correctWords = MutableLiveData<List<GameLetter>> ()
    private val correctWords : LiveData<List<GameLetter>> = _correctWords

    private val _validation = MutableLiveData<Boolean>()
    val validation: LiveData<Boolean> = _validation

    // select and unselect Letters

    fun addLetter( letter : GameLetter ) {
        if (isSelectionFull()){
//            invalidatePickedLetters()
//            invalidatePickedWord()
        }else{
            if (letter !in getSelection())
            _pickedLetters.value = getSelection() + letter}
        setValidate(true)
        Log.d("GAMEMAN", getSelection().toString())
    }

    fun removeLetter( letter : GameLetter ) {
        if(!isSelectionEmpty())
            _pickedLetters.value = getSelection() - letter
    }

    fun addWordLetters() {
        if (!isSelectionValid()) return
        val entry = currentSelection.value?.get(0) ?: GameLetter(0, 0, "")
        val exit = currentSelection.value?.get(1) ?: GameLetter(0, 0, "")
        val offset = Offset.getOffset(entry, exit)
        var (entryRow, entryCol) = entry
        for (i in 0..offset.size) {
            _pickedWord.value = getPickedWord() +
                    GameLetter(entryRow, entryCol, "")
            entryRow -= offset.x
            entryCol -= offset.y
        }
        if (isWordValid()) {
            getPickedWord().forEach { gameLetter ->
                _correctWords.value = getCorrectWords() + gameLetter
//                invalidatePickedWord()
            }
        }

//        invalidatePickedLetters()
    }
    // getters
    fun getPickedWordString() : String {
        val letters = getPickedWord()
        var result = ""
        letters.forEach{
            val (row, col) = it
            result += board.board[row][col]
        }
        return result
    }
    fun getPickedWord() : List<GameLetter> {
        return pickedWord.value ?: emptyList()
    }

    fun getSelection() : List<GameLetter> {
        return currentSelection.value ?: emptyList()
    }

    fun getCorrectWords() : List<GameLetter> {
        return correctWords.value ?: emptyList()
    }

    // check if picked letters are valid
    // and supporting functions

    fun isSelectionValid() : Boolean {
        if (isSelectionEmpty()) return false
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

    private fun isWordValid() : Boolean {
        val letters = getPickedWord()
        var word  = ""
        letters.forEach { gameLetter: GameLetter ->
            val (row, col) = gameLetter
            val value : String = board.board[row][col]
            word = word.plus(value)
        }
        Log.d("GAMEMODEL", word.lowercase())
        if (word.lowercase() in board.words) return true
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
        setValidate(true)
    }

    private fun invalidatePickedWord() {
        _pickedWord.value = emptyList<GameLetter>()
        setValidate(true)
    }

    fun needsUpdate() : Boolean{
        return  validation.value ?: false
    }

    fun setValidate(value : Boolean){
        _validation.value = value
    }

}