package com.example.trackme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackme.UiElements.CircularProgressBar
import com.example.trackme.UiElements.NumberPicker
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@Composable
fun GymScreen_Jake (
    state: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit,
    navController: NavController
){
        if (state.sessionOver){
            navController.navigate("end")
            //navController.popBackStack(route = "start", inclusive = false, saveState = false )
        }
        Column (horizontalAlignment = Alignment.Start){
            Text(text = "#" + state.sessionId, modifier = Modifier.padding(5.dp))
        }
        Column (verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Row(modifier = Modifier) {
                Text(
                    text = state.name,
                    fontSize = 50.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(bottom = 40.dp, start = 5.dp)
                        .weight(6f)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Box(contentAlignment = Alignment.Center) {

                if(state.resting){
                    timer(state)
                }
                else{
                    completSet(onEvent = onEvent, state = state)
                }
                CircularProgressBar(angle = (state.setNum.toFloat() / state.totalNumSetsTodo) * 360f, animator = state.percentage)
            }
            Box (contentAlignment = Alignment.Center,
                modifier = Modifier.padding(top = 50.dp)){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 10.dp)) {
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(size = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.DarkGray,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(width = 120.dp, height = 60.dp)
                        ) {}
                        NumberPicker(
                            value = state.repsCompleted,
                            onValueChange = {
                                onEvent(ExerciseEvent.RepChange(it))
                            },
                            range = 1..5,
                            dividersColor = Color.Transparent
                        )
                        Text(text = "Reps", modifier = Modifier.padding(end = 80.dp, bottom = 80.dp))
                    }
                    DisplayBox(label = "Weight", buttonContend = state.weight.toString())
                }
            }
        }
}


@Composable
fun completSet(onEvent: (ExerciseEvent) -> Unit, state: ExerciseState){
    Button(
        onClick = { onEvent(ExerciseEvent.FinishedSet)},
        modifier = Modifier.size(220.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background, contentColor = Color.White )
    ) {
        Text(text = state.circelLable.trimMargin(),
            fontSize = 40.sp,)
    }
}

@Composable
fun timer(state: ExerciseState){
    CircularProgressBar(size = 220.dp, angle = 360f, animator = state.timerPercent)

    Button(
        onClick = { },
        modifier = Modifier.size(220.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background, contentColor = Color.White )
    ) {
        Text(text = formatTime(state.restTimerCoutdown),
            fontSize = 30.sp,)
    }
}

@Composable
fun DisplayBox(label: String, buttonContend: String) {
    Row (verticalAlignment = Alignment.CenterVertically){
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 10.dp)){
            Button(onClick = { /*TODO*/ },
                shape = RoundedCornerShape(size = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray, contentColor = Color.White ),
                modifier = Modifier.size(width = 120.dp, height = 60.dp)) {
                Text(text = buttonContend,
                    fontSize = 25.sp)
            }
            Text(text = "Weight (kg)", modifier = Modifier.padding(end = 40.dp, bottom = 80.dp))
        }
    }
}

fun formatTime(secTime: Long):String{
    val duration = secTime.toDuration(DurationUnit.MILLISECONDS)
    val mins = duration.inWholeMinutes
    val secs = duration.minus(mins.toDuration(DurationUnit.MINUTES)).inWholeSeconds
        if (secs < 10)
        {
            return "$mins:0$secs"
        }
        return "$mins:$secs"
}






