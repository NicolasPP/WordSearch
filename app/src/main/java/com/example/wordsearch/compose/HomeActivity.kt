package com.example.wordsearch.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordsearch.R
import com.example.wordsearch.compose.navigation.BottomNavigationScreen
import com.example.wordsearch.compose.themes.colorDarkPalette
import com.example.wordsearch.compose.themes.colorLightPalette

const val EN_WIKI : String = "enwiki"
const val ES_VOCAB : String = "es"
const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val isDarkTheme = remember {
                mutableStateOf(true)
            }

            MaterialTheme (
                colors = if (isDarkTheme.value) colorDarkPalette else colorLightPalette
                    ){
                //                Main home page column
                val rNavController = rememberNavController()
                val backGroundColor = MaterialTheme.colors.background
                Scaffold(
                    bottomBar = { AddBottomAppBar(rNavController) },
                    floatingActionButtonPosition = FabPosition.Center,
                    isFloatingActionButtonDocked = true,
                    floatingActionButton = { AddPlayButton() }
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
                    shape = RectangleShape))

        Image(
            painter = painterResource(id = R.drawable.search_word),
            contentDescription = "Logo",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            modifier = Modifier
                .size(250.dp, 250.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.background,
                    shape = RectangleShape))

    }

}


@Composable
fun AddAppLogo(){}
@Composable
fun AddPlayButton(){
    FloatingActionButton(
        onClick = { /*TODO*/ }
    ) {
        Image(
            painter = painterResource(id = R.drawable.play_arrow_48px),
            contentDescription = "play",
            alignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .background(MaterialTheme.colors.primary),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.background))
    }
}
@Composable
fun AddNavContent(
    navController: NavHostController,
    isDarkTheme: MutableState<Boolean>
) {
    NavHost(navController, startDestination = BottomNavigationScreen.Home.route) {
        composable(BottomNavigationScreen.LeaderBoard.route) {
//           AddLeaderBoardScreen(isDarkTheme = isDarkTheme)
            AddSettingsScreen(isDarkTheme = isDarkTheme)
        }
        

        composable(BottomNavigationScreen.Home.route) {
            AddHomePage(isDarkTheme = isDarkTheme)
        }
    }
}
@Composable
fun AddBottomAppBar(nhc : NavHostController){

    val pages = listOf(
        BottomNavigationScreen.Home,
        BottomNavigationScreen.LeaderBoard
    )

    val currentIndex = remember { mutableStateOf(0) }

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.background),
        cutoutShape = MaterialTheme.shapes.small.copy(
            CornerSize(percent = 50)
        )
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
//                label = {
//                    Text(
//                        stringResource(id = page.stringResId)
//                    )
//                },
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