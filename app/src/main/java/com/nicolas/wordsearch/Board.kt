package com.nicolas.wordsearch

import android.content.Context
import java.io.IOException

// INPUT : input, SIZE : size, WORDS : words, KEY : key, BOARD : board

const val FILE = "local_data.json"

data class Board (
    val input : String,
    val size : Int,
    val words : List<String>,
    val board : List<List<String>>
        ) {
}

fun getData(
    context : Context
) : String? {
    val jsonString : String
    try {
        jsonString = context.assets.open(FILE).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}


const val input = "countless"
const val size = 19

val game_board = listOf(
    listOf("U","C","S","S","E","L","R","E","B","M","U","N","R","V","D","N","U","S","R"),
    listOf("O","J","X","L","H","F","H","M","C","P","Z","V","L","X","Y","V","C","O","A"),
    listOf("V","E","U","E","I","B","D","S","U","G","C","K","E","K","B","F","B","K","F"),
    listOf("W","H","P","V","Y","C","V","K","C","K","M","U","J","L","Y","P","Q","H","M"),
    listOf("I","N","N","U","M","E","R","A","B","L","E","Y","Q","S","C","G","N","C","I"),
    listOf("G","M","S","U","O","N","I","D","U","T","I","T","L","U","M","Q","J","E","N"),
    listOf("O","D","H","T","J","M","O","L","S","X","W","G","V","Q","A","Y","K","L","C"),
    listOf("H","X","S","R","P","T","I","F","C","H","U","F","O","U","D","J","S","B","A"),
    listOf("L","J","M","K","M","W","S","J","D","M","N","V","I","V","B","X","U","A","L"),
    listOf("V","H","W","I","Y","C","T","G","E","Q","N","D","N","X","N","K","O","R","C"),
    listOf("K","E","C","X","R","B","Z","Q","R","O","U","E","F","H","P","X","R","E","U"),
    listOf("L","Y","A","P","I","X","L","X","E","P","M","T","I","Y","Q","L","E","B","L"),
    listOf("J","W","E","J","A","Y","Q","N","B","Q","E","N","N","V","M","V","M","M","A"),
    listOf("V","K","H","X","D","S","X","R","M","X","R","U","I","Z","P","A","U","U","B"),
    listOf("D","J","U","R","F","L","M","Q","U","K","A","O","T","U","Y","Q","N","N","L"),
    listOf("I","C","A","M","C","S","Z","E","N","D","B","C","E","X","I","L","N","N","E"),
    listOf("J","R","E","D","Q","F","X","O","N","Q","L","N","L","W","D","M","I","U","D"),
    listOf("H","M","C","R","Y","I","E","K","U","R","E","U","S","X","Q","W","G","X","I"),
    listOf("U","G","W","X","L","S","Q","A","Y","P","L","F","T","A","J","C","F","P","J")
)

val words = listOf(
    "myriad",
    "infinite",
    "innumerable",
    "multitudinous",
    "uncounted",
    "incalculable",
    "innumerous",
    "numberless",
    "unnumberable",
    "unnumbered",
    "unnumerable"
)
fun getSample() : Board{
    return Board(
        input = input,
        size = size,
        words = words,
        board = game_board
    )
}

