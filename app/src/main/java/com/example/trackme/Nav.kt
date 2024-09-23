package com.example.trackme
import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import com.example.trackme.Gym.ExerciseViewModel
import com.example.trackme.Gym.GymScreen_Jake
import com.example.trackme.Start.Start
import com.example.trackme.end.EndViewModel
import com.example.trackme.end.End


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun Nav (
    mainFactory: ViewModelProvider.Factory,
    endFactory: ViewModelProvider.Factory
){

    val navHost = rememberNavController()


    NavHost(
        navController = navHost,
        startDestination = "start",
        modifier = Modifier,
        exitTransition = { ExitTransition.None},
        //enterTransition = { EnterTransition.None}
    ){
        composable(route = "start"){
            println(navHost.currentDestination)
            Start(navController = navHost)
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
            val endViewModel: EndViewModel =  viewModel(viewModelStoreOwner = test, factory = endFactory)
            val endState by endViewModel.state.collectAsState()
            End(navController = navHost, onEvent = endViewModel::onEvent,  state = endState)
        }
    }
}