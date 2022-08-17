package com.nicolas.wordsearch.gamelogic

class GameLetter(
     val row : Int,
     val col : Int,
     val value : String
){

    override fun equals(other: Any?): Boolean {
        if (other !is GameLetter) return false
        return ( row == other.row &&
                col == other.col)
    }
    override fun toString(): String {
        return "$value {row : $row col : $col}"
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        result = 31 * result + value.hashCode()
        return result
    }

    operator fun component1(): Int {
        return row
    }
    operator fun component2(): Int {
        return col
    }

}
