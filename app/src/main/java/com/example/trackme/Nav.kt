package com.example.trackme
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun Nav (
    mainFactory: ViewModelProvider.Factory,
    endFactory: ViewModelProvider.Factory
){

    val navHost = rememberNavController()
    println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTt")
    //println(navHost.currentDestination)


    NavHost(
        navController = navHost,
        startDestination = "start",
        modifier = Modifier
    ){
        composable(route = "start"){
            println(navHost.currentDestination)
            start(navController = navHost)
        }
        composable(route = "session"){
            println(navHost.currentDestination)
            val test = remember {
                navHost.getBackStackEntry("session")
            }
            val sessionViewModel: ExerciseViewModel =  viewModel(viewModelStoreOwner = test, factory = mainFactory)
            val sessionState by sessionViewModel.state.collectAsState()
            GymScreen_Jake(state = sessionState, onEvent = sessionViewModel::onEvent, navController = navHost)
        }
        composable(route = "end"){
            println(navHost.currentDestination)

            val test = remember {
                navHost.getBackStackEntry("end")
            }
            val sessionViewModel: EndViewModel =  viewModel(viewModelStoreOwner = test, factory = endFactory)
            val endState by sessionViewModel.state.collectAsState()
            end(navController = navHost, state = endState)
        }
    }
}