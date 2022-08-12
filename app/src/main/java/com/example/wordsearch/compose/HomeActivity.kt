package com.example.wordsearch.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordsearch.R
import com.example.wordsearch.compose.navigation.BottomNavigationScreen

const val EN_WIKI : String = "enwiki"
const val ES_VOCAB : String = "es"
const val TAG = "HomeActivity"
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AddHomePage()
            }
        }
    }
}
@Composable
fun AddHomePage(){
    val rNavController = rememberNavController()
//                Main home page column
    Scaffold(
        bottomBar = {
            AddBottomNavigationBar(rNavController)
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
                .background(Color.Black)
        ){
            AddNavContent(navController = rNavController)
        }
    }
}
@Composable
fun AddAppLogo(){}
@Composable
fun AddPlayButton(){}
@Composable
fun AddNavContent(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = BottomNavigationScreen.Home.route) {
        composable(BottomNavigationScreen.Home.route) {
//            AddHomePage()
        }

        composable(BottomNavigationScreen.LeaderBoard.route) {
           AddFavouritesScreen()
        }

        composable(BottomNavigationScreen.Settings.route) {
            AddFavouritesScreen()
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
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.DarkGray,
                alwaysShowLabel = true,
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

@Composable
fun AddFavouritesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(id = R.string.Home),
            color = Color.White,
            fontSize = 19.sp,
            fontStyle = FontStyle.Normal
        )
    }
}