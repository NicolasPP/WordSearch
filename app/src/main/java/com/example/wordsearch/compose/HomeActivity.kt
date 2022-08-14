package com.example.wordsearch.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                Scaffold(
                    bottomBar = { AddBottomNavigationBar(rNavController) },
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
    isDarkTheme: MutableState<Boolean>
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ){
        Image(
            painter = painterResource(id = if (isDarkTheme.value)
                R.drawable.app_logo_black_background else R.drawable.app_logo_white_background),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp, 200.dp)
                .border(1.dp, color = MaterialTheme.colors.primary, shape = RectangleShape))


//        Button(
//            onClick = {},
//            shape = RoundedCornerShape(25.dp),
//            modifier = Modifier
//                .width(200.dp)
//                .background(MaterialTheme.colors.primary),
//            border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
//        ){
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth(),
//
//                ){
//                Image(
//                    painter = painterResource(id = R.drawable.play_arrow_48px),
//                    contentDescription = "play",
//                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
//                    modifier = Modifier
//                        .size(24.dp)
//                )
//            }
//        }
    }

}


@Composable
fun AddAppLogo(){}
@Composable
fun AddPlayButton(){
    FloatingActionButton(onClick = { /*TODO*/ }) {
        Image(painter = painterResource(id = R.drawable.play_arrow_48px), contentDescription = "play")
    }
}
@Composable
fun AddNavContent(
    navController: NavHostController,
    isDarkTheme: MutableState<Boolean>
) {
    NavHost(navController, startDestination = BottomNavigationScreen.Home.route) {
        composable(BottomNavigationScreen.Home.route) {
            AddHomePage(isDarkTheme = isDarkTheme)
        }

        composable(BottomNavigationScreen.LeaderBoard.route) {
           AddLeaderBoardScreen(isDarkTheme = isDarkTheme)
        }

        composable(BottomNavigationScreen.Settings.route) {
            AddSettingsScreen(isDarkTheme = isDarkTheme)
        }
    }
}
@Composable
fun AddBottomNavigationBar(nhc : NavHostController){
    
    val pages = listOf(
        BottomNavigationScreen.Home,
        BottomNavigationScreen.LeaderBoard,
        BottomNavigationScreen.Settings
    )
    
    val currentIndex = remember { mutableStateOf(0) }
    
    BottomNavigation(
        modifier = Modifier
            .background(Color.White)
    ) {
        pages.forEachIndexed{ pageIndex, page ->
            
            val isPageSelected = currentIndex.value == pageIndex
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource( id = page.drawResId ),
                        contentDescription = stringResource(id = page.stringResId),
                    )
                },
                label = {
                    Text(
                        stringResource(id = page.stringResId)
                    )
                },
                selected = isPageSelected,
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.secondary,
                alwaysShowLabel = true,
                modifier =Modifier
                    .background(MaterialTheme.colors.primary),
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