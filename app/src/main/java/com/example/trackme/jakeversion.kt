package com.example.trackme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun GymScreen_Jake (
    state: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit
){
    Column (horizontalAlignment = Alignment.Start){
        Text(text = "#" + state.sessionId, modifier = Modifier.padding(5.dp))
    }
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = state.name,
            fontSize = 50.sp,
            color = Color.White,
            modifier = Modifier.padding(end = 200.dp, bottom = 40.dp))
        Box(contentAlignment = Alignment.Center) {

            if(state.resting){
                val restTime: Int = 90
                val animationRate: Double = 0.5
                val delayRate: Double = (animationRate / (360/restTime).toDouble())
                var ticks by remember { mutableIntStateOf(0) }
                var time by remember { mutableIntStateOf(restTime) }

                LaunchedEffect(Unit) {
                    while (360f - animationRate * ticks > 0) {
                        delay(delayRate.seconds)
                        ticks++
                    }
                    onEvent(ExerciseEvent.NextExercise)
                }
                LaunchedEffect(Unit) {
                    while (time > 0) {
                        delay(1.seconds)
                        time--
                    }
                }
                CircularProgressBar(size = 220.dp, angle = (360f - animationRate * ticks).toFloat())

                Button(
                    onClick = { },
                    modifier = Modifier.size(220.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background, contentColor = Color.White )
                ) {
                        Text(text = formatTime(time),
                            fontSize = 30.sp,)
                    }
            }
            else{
                Button(
                    onClick = { if(state.lastSet){ onEvent(ExerciseEvent.FinishedExercise)} else{onEvent(ExerciseEvent.FinishedSet) }},
                    modifier = Modifier.size(220.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background, contentColor = Color.White )
                ) {
                    Text(text = """Start
                        |
                        |Rest""".trimMargin(),
                        fontSize = 40.sp,)
                }
            }
            CircularProgressBar(angle = ((state.set.toFloat()) / 5) * 360f)
            //BottomToTopFilledCircle(progress = 0.5f)
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
                            value = state.rep,
                            onValueChange = {
                                onEvent(ExerciseEvent.RepChange(it))
                            },
                            range = 1..5,
                            dividersColor = Color.Transparent
                        )
                        Text(text = "Reps", modifier = Modifier.padding(end = 80.dp, bottom = 80.dp))
                    }

                    DisplayBox(label = "Weight", buttonContend = formatWeight(state.weight) + "kg")

                }

            }
            //DisplayBox(label = "Reps", buttonContend = state.rep.toString())


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
            Text(text = "Weight", modifier = Modifier.padding(end = 80.dp, bottom = 80.dp))

        }
    }
}

fun formatTime(secTime: Int):String{
        if (secTime%60 < 10)
        {
            return (secTime/60).toString() + ":0" + secTime%60
        }

        return (secTime/60).toString() + ":" + secTime%60
}





