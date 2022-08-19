package com.nicolas.wordsearch.compose


import androidx.compose.foundation.Image
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nicolas.wordsearch.R
import com.nicolas.wordsearch.compose.themes.colorDarkPalette
import com.nicolas.wordsearch.compose.themes.colorLightPalette
import com.nicolas.wordsearch.data.repository.AppPreferences
import com.nicolas.wordsearch.gamelogic.GameLetter
import com.nicolas.wordsearch.getSample
import com.nicolas.wordsearch.model.GameManagerViewModel
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Box as Box

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appPreferences = AppPreferences(this)
        val gameManModel = GameManagerViewModel(getSample())

        setContent{
            val isPaused = remember { mutableStateOf(false) }
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
    val currentSelection = remember { mutableStateOf<List<GameLetter>>( emptyList() )}
    val pressedList = remember { mutableStateOf<List<GameLetter>>( emptyList() )}
    val correctList = remember { mutableStateOf<List<GameLetter>>( emptyList() )}
    MaterialTheme(
        colors = if (isDarkThemeVal) colorDarkPalette else colorLightPalette
    ){
        Scaffold(
            bottomBar = { AddGameBottomBar(isPaused = isPaused, currentSelection, gameManModel) },
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
                AddGameScaffoldContent(gameManModel = gameManModel, pressedList, currentSelection, correctList )
            }
        }
    }
}



@Composable
fun AddGameBottomBar(
    isPaused: MutableState<Boolean>,
    currentSelection: MutableState<List<GameLetter>>,
    gameMan : GameManagerViewModel
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
        var prefix = getLetterString(currentSelection, gameMan)
        val suffix = " is not on the board"
        val message = if (prefix.isNotEmpty()) prefix + suffix else prefix
        AddGameTimer(isPaused = isPaused)
        Divider(modifier = Modifier.width(20.dp))
        Text(text = message, color = MaterialTheme.colors.primary)
    }
}
fun getLetterString(
    currentSelection : MutableState<List<GameLetter>>,
    gameMan : GameManagerViewModel
) : String{
    var word = ""
    currentSelection.value.forEach{
        val (row, col) = it
        val value = gameMan.board.board[row][col]
        word += value
    }
    Log.d("GAME", word)
    return word
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
                        contentDescription = "return",
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

fun doNothing() {}

fun launchHome(context : Context) {
    val intent = Intent(context, GameConfigActivity::class.java)
    context.startActivity(intent)
}

@Composable
fun AddGameScaffoldContent(
    gameManModel: GameManagerViewModel,
    pressedList: MutableState<List<GameLetter>>,
    currentSelection: MutableState<List<GameLetter>>,
    correctList: MutableState<List<GameLetter>>
){
    AddGame(gameManModel.board.board, gameManModel, pressedList, currentSelection, correctList)
}

@Composable
fun AddGame(
    board : List<List<String>>,
    gameManModel : GameManagerViewModel,
    pressedList : MutableState<List<GameLetter>>,
    currentWord : MutableState<List<GameLetter>>,
    correctWords : MutableState<List<GameLetter>>
){

    val rowSize = board.size
    val colSize = board.get(0).size
    LazyVerticalGrid(columns = GridCells.Fixed(colSize)){
        val isSelectionValid = gameManModel.isSelectionValid()
        board.forEachIndexed{rowIndex, list ->
            list.forEachIndexed { colIndex, letter ->

                val currentLetter = GameLetter(rowIndex, colIndex, letter)
                val isPresentInState = (currentLetter in pressedList.value)
                val isInCorrectWords = (currentLetter in correctWords.value)

                Log.d("GAMEMAN", pressedList.value.toString())
                item(){
                    val backgroundColor =  if (isPresentInState)  Color.Red else Color.Transparent
                    val textColor = if (isInCorrectWords)
                        MaterialTheme.colors.onBackground else MaterialTheme.colors.primary
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ){
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(backgroundColor)
                                .clickable(
                                    enabled = true
                                ) {
//                                    Log.d("GAME",currentWord.value.toString())
//                                    Log.d("GAME",pressedList.value.toString())
//                                    Log.d("GAME",correctWords.value.toString())
                                    if (isPresentInState) {
                                        gameManModel.removeLetter(currentLetter)
//                                        pressedList.value = gameManModel.getSelection()
                                    } else {
                                        gameManModel.addLetter(currentLetter)
//                                        pressedList.value = gameManModel.getSelection()
//                                        gameManModel.addWordLetters()
//                                        currentWord.value = gameManModel.getPickedWord()
//                                        correctWords.value = gameManModel.getCorrectWords()
                                    }
                                },
                            text = letter,
                            color = textColor
                        )
                        if (gameManModel.needsUpdate()){
//                            gameManModel.addWordLetters()
                            pressedList.value = gameManModel.getSelection()
                            currentWord.value = gameManModel.getPickedWord()
                            correctWords.value = gameManModel.getCorrectWords()
                            gameManModel.setValidate(false)
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun AddGameProgress(

){

}

@Composable
fun AddGameTimer(
    isPaused : MutableState<Boolean>
){


    val timePassed = remember {
        mutableStateOf(0L)
    }

    LaunchedEffect(key1 = timePassed.value, key2 = isPaused.value){
    while (!isPaused.value) {
        delay(100L)
        timePassed.value += 100L

    }
}


    Row(
        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(id =R.drawable.outline_timer_24),
            contentDescription = "Timer",
            modifier = Modifier,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.background)
        )
        Divider(Modifier.width(15.dp), color= Color.Transparent)
        Text(
            modifier = Modifier,
            text = milliSecsToMin(timePassed.value).toString(),
            color = MaterialTheme.colors.primary,
        )
    }
}

fun milliSecsToMin(milli: Long): Any {
    val minutes = milli / 1000 / 60
    val seconds = milli / 1000 % 60
    return object {
        override fun toString() = "$minutes : $seconds"
    }
}
