package com.nicolas.wordsearch.compose


import androidx.compose.foundation.Image
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            val isDone = remember { mutableStateOf(false) }
            AddMainContent(
                isDarkThemeVal = appPreferences.getDarkTheme(),
                isPaused = isPaused,
                gameManModel = gameManModel,
                isDone = isDone
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
fun AddMainContent(
    isDarkThemeVal: Boolean,
    isPaused : MutableState<Boolean>,
    gameManModel : GameManagerViewModel,
    isDone : MutableState<Boolean>
){
    if (!isDone.value){
        AddGameContent(isDarkThemeVal = isDarkThemeVal, isPaused = isPaused, gameManModel = gameManModel, isDone = isDone)
    }else{
        launchHome(LocalContext.current)
    }
}

@Composable
fun AddGameContent(
    isDarkThemeVal : Boolean,
    isPaused : MutableState<Boolean>,
    gameManModel : GameManagerViewModel,
    isDone : MutableState<Boolean>
){
    val currentSelection = remember { mutableStateOf<List<GameLetter>>( emptyList() )}
    val pressedList = remember { mutableStateOf<List<GameLetter>>( emptyList() )}
    val correctList = remember { mutableStateOf<List<GameLetter>>( emptyList() )}
    val foundWordsNum = remember { mutableStateOf(0) }
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
            topBar = { AddGameTopBar(isPaused = isPaused, wordCount = gameManModel.board.words.size, foundWordsNum) },
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
                AddGameScaffoldContent(gameManModel = gameManModel, pressedList, currentSelection, correctList, foundWordsNum, isPaused, isDone)
            }
        }
    }
}



@Composable
fun AddGameBottomBar(
    isPaused: MutableState<Boolean>,
    currentSelection: MutableState<List<GameLetter>>,
    gameMan : GameManagerViewModel,
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
        var word = getLetterString(currentSelection, gameMan)
        AddGameTimer(isPaused = isPaused)
        Divider(modifier = Modifier.width(20.dp))
        Text(text = word, color = MaterialTheme.colors.primary)
        Divider(modifier = Modifier.width(20.dp))
        val check = if (gameMan.isWordValid()) R.drawable.outline_check_24 else R.drawable.outline_clear_24
        val tint = if (gameMan.isWordValid()) Color.Green else Color.Red
        if (!currentSelection.value.isEmpty()) {
            Image(painter = painterResource(id = check), contentDescription = "check", colorFilter = ColorFilter.tint(tint) )
        }
        if (isPaused.value){
            currentSelection.value = emptyList()
        }
        gameMan.invalidatePickedWord()
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
    return word
}

@Composable
fun AddGameTopBar(
    isPaused: MutableState<Boolean>,
    wordCount : Int,
    foundWordsNum : MutableState<Int>
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
        Row(
//            horizontalArrangement = Arrangement.spacedBy(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 10.dp)
        ){
            CompositionLocalProvider(LocalRippleTheme provides
                    if (isPaused.value) LocalRippleTheme.current else NoRippleTheme
            ){
                FloatingActionButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = { if (isPaused.value) doNothing() else
                        launchGameConfig(context)},
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.outline_arrow_back_24),
                        contentDescription = "return",
                        modifier = Modifier
                            .background(
                                if (isPaused.value) MaterialTheme.colors.onError
                                else MaterialTheme.colors.primary
                            )
                            .size(40.dp),
                        colorFilter = ColorFilter.tint( if (isPaused.value) MaterialTheme.colors.onError
                        else MaterialTheme.colors.background))
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                        ){

                    val text = foundWordsNum.value.toString() + " / $wordCount"
                    Text(
                        modifier = Modifier,
                        text = text,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

fun doNothing() {}

fun launchGameConfig(context : Context) {
    val intent = Intent(context, GameConfigActivity::class.java)
    context.startActivity(intent)
}

@Composable
fun AddGameScaffoldContent(
    gameManModel: GameManagerViewModel,
    pressedList: MutableState<List<GameLetter>>,
    currentSelection: MutableState<List<GameLetter>>,
    correctList: MutableState<List<GameLetter>>,
    foundWordsNum : MutableState<Int>,
    isPaused : MutableState<Boolean>,
    isDone : MutableState<Boolean>
){
    AddGame(gameManModel.board.board, gameManModel, pressedList, currentSelection, correctList, foundWordsNum, isPaused, isDone)
    if (isPaused.value){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "GAME PAUSED", color = MaterialTheme.colors.primary)
        }
    }
}

@Composable
fun AddGame(
    board : List<List<String>>,
    gameManModel : GameManagerViewModel,
    pressedList : MutableState<List<GameLetter>>,
    currentWord : MutableState<List<GameLetter>>,
    correctWords : MutableState<List<GameLetter>>,
    foundWordsNum : MutableState<Int>,
    isPaused: MutableState<Boolean>,
    isDone : MutableState<Boolean>
){

    val rowSize = board.size
    val colSize = board.get(0).size
    LazyVerticalGrid(columns = GridCells.Fixed(colSize)){
        board.forEachIndexed{rowIndex, list ->
            list.forEachIndexed { colIndex, letter ->

                val currentLetter = GameLetter(rowIndex, colIndex, letter)
                val isPresentInState = (currentLetter in pressedList.value)
                val isInCorrectWords = (currentLetter in correctWords.value)

                item(){
                    val backgroundColor = if (isPresentInState)  MaterialTheme.colors.onBackground else Color.Transparent
                    val txtColor = if (isInCorrectWords)
                        MaterialTheme.colors.onBackground else MaterialTheme.colors.primary
                    val textColor = if (isPaused.value) MaterialTheme.colors.secondary else txtColor
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
                                    if (!isPaused.value) {
                                        if (isPresentInState) {
                                            gameManModel.removeLetter(currentLetter)
                                        } else {
                                            gameManModel.addLetter(currentLetter)
                                        }
                                        if (gameManModel.needsUpdate()) {
                                            gameManModel.addWordLetters()
                                            foundWordsNum.value = gameManModel.foundWords.size
                                            pressedList.value = gameManModel.getSelection()
                                            currentWord.value = gameManModel.getPickedWord()
                                            correctWords.value = gameManModel.getCorrectWords()
                                            isDone.value = gameManModel.isDone()
                                            gameManModel.setValidate(false)
                                        }
                                    }
                                },
                            text = letter,
                            color = textColor
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
