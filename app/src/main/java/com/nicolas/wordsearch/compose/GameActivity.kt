package com.nicolas.wordsearch.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.nicolas.wordsearch.R
import com.nicolas.wordsearch.compose.themes.colorDarkPalette
import com.nicolas.wordsearch.compose.themes.colorLightPalette
import com.nicolas.wordsearch.data.repository.AppPreferences
import com.nicolas.wordsearch.gamelogic.GameManager
import com.nicolas.wordsearch.gamelogic.GameLetter
import com.nicolas.wordsearch.model.GameManagerViewModel
import androidx.compose.foundation.layout.Box as Box

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appPreferences = AppPreferences(this)
        val gameManModel = GameManagerViewModel()
        setContent{
            val isPaused = remember { mutableStateOf(true) }
            AddGameContent(
                isDarkThemeVal = appPreferences.getDarkTheme(),
                isPaused = isPaused,
                gameManModel = gameManModel
            )
        }
    }
}

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f,0.0f,0.0f,0.0f)
}

@Composable
fun AddGameContent(
    isDarkThemeVal : Boolean,
    isPaused : MutableState<Boolean>,
    gameManModel : GameManagerViewModel
){
    MaterialTheme(
        colors = if (isDarkThemeVal) colorDarkPalette else colorLightPalette
    ){
        Scaffold(
            bottomBar = { AddGameBottomBar(isPaused = isPaused) },
            floatingActionButton = {
                AddFloatingAction(
                    iconId = if (isPaused.value) R.drawable.play_arrow_48px else R.drawable.outline_pause_24,
                    dpSize = 56,
                    bgColor = MaterialTheme.colors.primary,
                    fgColor = MaterialTheme.colors.background,
                    cDescription = "pause",
                    modifier = Modifier
                ){
                    isPaused.value = ! isPaused.value
                }},
            topBar = { AddGameTopBar(isPaused = isPaused) },
            floatingActionButtonPosition = FabPosition.End,
            isFloatingActionButtonDocked = true,
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top= 16.dp,
                        bottom = it.calculateBottomPadding()
                    )
            ){
                AddGameScaffoldContent(gameManModel = gameManModel)
            }
        }
    }
}

@Composable
fun AddGameBottomBar(
    isPaused: MutableState<Boolean>
){
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.background),
        cutoutShape = MaterialTheme.shapes.small.copy(
            CornerSize(percent = 50)
        ),
        backgroundColor = MaterialTheme.colors.onBackground
    ){
//        add timer here
    }
}

@Composable
fun AddGameTopBar(
    isPaused: MutableState<Boolean>
){
    val context = LocalContext.current
    TopAppBar(
        backgroundColor  = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(30.dp))
            .background(MaterialTheme.colors.onBackground)
            .padding(start = 8.dp, end = 8.dp)
    ){
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 10.dp)
        ){
            CompositionLocalProvider(LocalRippleTheme provides
                    if (isPaused.value) LocalRippleTheme.current else NoRippleTheme
            ){
                FloatingActionButton(
                    modifier = Modifier
                        .size(56.dp),
                    onClick = { if (isPaused.value) doNothing() else
                        launchHome(context)},
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.outline_arrow_back_24),
                        contentDescription = "Localized description",
                        modifier = Modifier
                            .background(
                                if (isPaused.value) MaterialTheme.colors.onError
                                else MaterialTheme.colors.primary
                            )
                            .size(56.dp),
                        colorFilter = ColorFilter.tint( if (isPaused.value) MaterialTheme.colors.onError
                        else MaterialTheme.colors.background))
                }
            }
        }
    }
}

fun doNothing() {

}

fun launchHome(context : Context) {
    val intent = Intent(context, GameConfigActivity::class.java)
    context.startActivity(intent)
}

@Composable
fun AddGameScaffoldContent(
    gameManModel: GameManagerViewModel
){
    val testBoard = listOf(
        listOf("1", "2" ,"3","4", "5" ,"6", "8", "9" ,"10","8", "9" ,"10"),
        listOf("4", "5" ,"6","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10"),
        listOf("8", "9" ,"10","8", "9" ,"10","8", "9" ,"10","8", "9" ,"10"),
        listOf("1", "2" ,"3","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10" ),
        listOf("4", "5" ,"6","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10"),
        listOf("8", "9" ,"10","8", "9" ,"10","8", "9" ,"10","8", "9" ,"10"),
        listOf("1", "2" ,"3","4", "5" ,"6", "8", "9" ,"10","8", "9" ,"10"),
        listOf("4", "5" ,"6","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10"),
        listOf("8", "9" ,"10","8", "9" ,"10","8", "9" ,"10","8", "9" ,"10"),
        listOf("1", "2" ,"3","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10" ),
        listOf("4", "5" ,"6","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10"),
        listOf("8", "9" ,"10","8", "9" ,"10","8", "9" ,"10","8", "9" ,"10"),
        listOf("1", "2" ,"3","4", "5" ,"6", "8", "9" ,"10","8", "9" ,"10"),
        listOf("4", "5" ,"6","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10"),
        listOf("8", "9" ,"10","8", "9" ,"10","8", "9" ,"10","8", "9" ,"10"),
        listOf("1", "2" ,"3","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10" ),
        listOf("4", "5" ,"6","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10"),
        listOf("8", "9" ,"10","8", "9" ,"10","8", "9" ,"10","8", "9" ,"10"),
        listOf("1", "2" ,"3","4", "5" ,"6", "8", "9" ,"10","8", "9" ,"10"),
        listOf("4", "5" ,"6","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10"),
        listOf("8", "9" ,"10","8", "9" ,"10","8", "9" ,"10","8", "9" ,"10"),
        listOf("1", "2" ,"3","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10" ),
        listOf("4", "5" ,"6","4", "5" ,"6","8", "9" ,"10","8", "9" ,"10"),
    )
    val currentSelection = remember { mutableStateOf<List<GameLetter>>( emptyList() )}
    val  pressedList = remember { mutableStateOf<List<GameLetter>>( emptyList() )}
    AddGame(testBoard, gameManModel, pressedList, currentSelection)
}

@Composable
fun AddGame(
    board : List<List<String>>,
    gameManModel : GameManagerViewModel,
    pressedList : MutableState<List<GameLetter>>,
    currentWord : MutableState<List<GameLetter>>
){

    val rowSize = board.size
    val colSize = board.get(0).size
    LazyVerticalGrid(columns = GridCells.Fixed(colSize)){
        board.forEachIndexed{rowIndex, list ->
            list.forEachIndexed { colIndex, letter ->
//                Log.d("HOMEAC", "row $rowIndex col $colIndex")
                val currentLetter = GameLetter(rowIndex, colIndex, letter)
                val isPresentInState = (currentLetter in pressedList.value)
                val isInCurrentSelection = (currentLetter in currentWord.value)
                val isSelectionValid = gameManModel.isSelectionValid()
                val word = if (isSelectionValid) gameManModel.getPickedWord() else emptyList<GameLetter>()
                if (isSelectionValid){
                    gameManModel.addWordLetters()
                    currentWord.value = word
                }
                val preCol = if (isSelectionValid) Color.Green else Color.Red
                val col = if (isPresentInState) preCol else Color.Transparent
                val c = if (isInCurrentSelection) Color.Blue else col
                item(){
                    Box(
                        modifier = Modifier
                            .border(BorderStroke(1.dp, c))
                    ){
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clickable(
                                    enabled = true
                                ){
                                    if (isPresentInState) {
                                        Log.d("HOME", "I AM HERE")
                                        gameManModel.removeLetter(currentLetter)
                                        pressedList.value = gameManModel.getSelection()
                                    } else{
                                        gameManModel.addLetter(currentLetter)
                                        pressedList.value = gameManModel.getSelection()
                                    }
                                },
                            text = letter
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun AddGameProgress(

){
//    PUT GAME HERE
}

@Composable
fun AddGameTimer(

){
//    PUT GAME HERE
}
