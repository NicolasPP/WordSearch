package com.nicolas.wordsearch.compose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nicolas.wordsearch.*
import com.nicolas.wordsearch.R
import com.nicolas.wordsearch.compose.navigation.HomeNavigation
import com.nicolas.wordsearch.compose.themes.colorDarkPalette
import com.nicolas.wordsearch.compose.themes.colorLightPalette
import com.nicolas.wordsearch.data.DataMuseResult
import com.nicolas.wordsearch.data.repository.AppPreferences
import com.nicolas.wordsearch.model.WordItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.IOException

const val EN_WIKI : String = "enwiki"
const val ES_VOCAB : String = "es"
const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity(), DataMuseResult {
    var w = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appPreferences = AppPreferences(this)
        setContent {
            val isDarkTheme = remember {
                mutableStateOf(appPreferences.getDarkTheme())
            }
            //val jsonString = getData(this); val b : List<BoardItem> = Json.decodeFromString(jsonString ?: "")
            lateinit var jsonString: String
            try {
                jsonString = baseContext.assets.open("local_data.json")
                    .bufferedReader()
                    .use {it.readText() }

                AppApplication().repository.insertItems(Json.decodeFromString(jsonString))
            } catch(excep: IOException) {

            }

            //Log.d(TAG, b.toString())

            isDarkTheme.value = appPreferences.getDarkTheme()
            val settingIconPos = remember {
                mutableStateOf(0F)
            }
            val rNavController = rememberNavController()
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()

            AddContent(
                isDarkTheme = isDarkTheme,
                scaffoldState = scaffoldState,
                scope = scope,
                settingIconPos = settingIconPos,
                rNavController =rNavController,
                appPreferences = appPreferences
            )

        }
    }

    override fun onDataFetchedSuccess(words: List<WordItem>) {
        w.add(words[0].word)
        Log.d(TAG, w.size.toString())
        Log.d(TAG, w.toString())
    }

    override fun onDataFetchedFailed() {
        Log.d(TAG, "fetch failed")
    }
}

@Composable
fun AddContent(
    isDarkTheme : MutableState<Boolean>,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    settingIconPos: MutableState<Float>,
    rNavController : NavHostController,
    appPreferences: AppPreferences

    ){
    val context = LocalContext.current
    MaterialTheme (
        colors = if (isDarkTheme.value) colorDarkPalette else colorLightPalette
    ){
        //                Main home page column
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                AddTopAppBar(
                    scaffoldState = scaffoldState,
                    scope = scope,
                    settingIconPos = if (scaffoldState.drawerState.isOpen) settingIconPos.value else 0.0f
                )},
            drawerContent = {
                AddSettingsScreen(isDarkTheme = isDarkTheme, settingIconPos, appPreferences)
            },
            bottomBar = { AddBottomAppBar(rNavController)},
            floatingActionButton = { AddFloatingAction(
                iconId = R.drawable.play_arrow_48px,
                dpSize = 56,
                bgColor = MaterialTheme.colors.primary,
                fgColor = MaterialTheme.colors.background,
                cDescription = "play",
                modifier = Modifier,
            ){
                val intent = Intent(context, GameConfigActivity::class.java)
                context.startActivity(intent)
            }},
            drawerShape = customShape(settingIconPos),
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true
        ){
            Column(
                modifier = Modifier
                    .padding(bottom = it.calculateBottomPadding())
            ){
                AddNavContent(navController = rNavController, isDarkTheme)
            }
        }
    }
}

@Composable
fun AddHomePage(
    isDarkTheme: MutableState<Boolean>,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){

        Image(
            painter = painterResource(id = if (isDarkTheme.value)
                R.drawable.app_logo_dark_background else R.drawable.app_logo_light_background),
            contentDescription = "Logo",
            modifier = Modifier
                .offset(y = 80.dp)
                .size(300.dp, 300.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.background,
                    shape = RectangleShape
                ))

        Image(
            painter = painterResource(id = R.drawable.search_word),
            contentDescription = "Logo",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            modifier = Modifier
                .size(250.dp, 250.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.background,
                    shape = RectangleShape
                ))

    }

}

@Composable
fun customShape(
    settingIconPos: MutableState<Float>
) = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        settingIconPos.value = size.width * 2 / 3

        return Outline.Rectangle(
            Rect(
                left = 0f,
                top = 0f,
                right = size.width * 2 / 3,
                bottom = size.height
            )
        )
    }
}

@Composable
fun AddFloatingAction(
    iconId : Int,
    dpSize : Int,
    bgColor : Color,
    fgColor : Color,
    cDescription : String,
    modifier: Modifier,
    onClick: () -> Unit
    ){
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = cDescription,
            alignment = Alignment.Center,
            modifier = Modifier
                .size(dpSize.dp)
                .background(bgColor),
            colorFilter = ColorFilter.tint(fgColor))
    }
}


@Composable
fun AddNavContent(
    navController: NavHostController,
    isDarkTheme: MutableState<Boolean>,
) {
    NavHost(navController, startDestination = HomeNavigation.Home.route) {
        composable(HomeNavigation.LeaderBoard.route) {
           AddLeaderBoardScreen(isDarkTheme = isDarkTheme)
        }

        composable(HomeNavigation.Home.route) {
            AddHomePage(isDarkTheme = isDarkTheme)
        }
    }
}

@Composable
fun AddTopAppBar(
    scaffoldState: ScaffoldState,
    scope : CoroutineScope,
    settingIconPos : Float
){
    TopAppBar(
        backgroundColor  = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(30.dp))
            .background(MaterialTheme.colors.onBackground)
            .padding(start = 8.dp, end = 8.dp)
            ){
        val size = with (LocalDensity.current){ settingIconPos.toDp()}
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = size)
        ){
            AddFloatingAction(
                iconId = R.drawable.menu_48px,
                dpSize = 50,
                bgColor = MaterialTheme.colors.primary,
                fgColor = MaterialTheme.colors.background,
                modifier = Modifier
                    .size(40.dp),
                cDescription = "Settings"){
                scope.launch {
                    scaffoldState.drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        }
    }
}

@Composable
fun AddBottomAppBar(nhc : NavHostController){

    val pages = listOf(
        HomeNavigation.Home,
        HomeNavigation.LeaderBoard
    )

    val currentIndex = remember { mutableStateOf(0) }

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.background),
        cutoutShape = MaterialTheme.shapes.small.copy(
            CornerSize(percent = 50)
        ),
    ) {

        pages.forEachIndexed{ pageIndex, page ->

            val isPageSelected = currentIndex.value == pageIndex
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource( id = page.drawResId ),
                        contentDescription = stringResource(id = page.stringResId),
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                selected = isPageSelected,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.secondary,
                alwaysShowLabel = true,
                modifier =Modifier
                    .background(MaterialTheme.colors.onBackground),
                onClick = {
                    if (!isPageSelected){
                        currentIndex.value = pageIndex
                        nhc.navigate(page.route) {
                            popUpTo(nhc.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}