package com.example.trackme.Gym

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackme.UiElements.CircularProgressBar
import kotlin.math.roundToLong
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun RestTimer(state: ExerciseState, onEvent: (ExerciseEvent) -> Unit){
    CircularProgressBar(size = 220.dp, angle = 360f, animator = state.timerPercent)


//        Text(
//            text = formatTime(state.timerPercent),
//            fontSize = 30.sp,
//        )

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.size(220.dp),

            ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatTime(state.timerPercent),
                    fontSize = 30.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                IconButton(
                    onClick = { onEvent(ExerciseEvent.SkipRest) },
                    modifier = Modifier.border(border = BorderStroke(width = 2.dp, color = Color.White), shape = CircleShape)
                ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null
                        )
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowRight,
//                            contentDescription = null
//                        )


                }
            }
        }
//        Button(
//            onClick = { onEvent(ExerciseEvent.SkipRest) },
//            modifier = Modifier.size(220.dp),
//            shape = CircleShape,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.background,
//                contentColor = Color.White
//            )
//        ) {
//            Text(
//                text = formatTime(state.timerPercent),
//                fontSize = 30.sp,
//            )
//        }

}


fun formatTime(percent: Float): String{
    val restTime: Long = (90000 * percent).roundToLong()  // temp hard code rest time

    val duration = restTime.toDuration(DurationUnit.MILLISECONDS)
    val mins = duration.inWholeMinutes
    val secs = duration.minus(mins.toDuration(DurationUnit.MINUTES)).inWholeSeconds
    if (secs < 10)
    {
        return "$mins:0$secs"
    }
    return "$mins:$secs"
}