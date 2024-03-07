package com.example.trackme




import BottomToTopFilledCircle
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun GymScreen (
    state: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit
){
    Column (horizontalAlignment = Alignment.Start){
        Text(text = "#" + state.sessionId, modifier = Modifier.padding(5.dp))
        Text(text = state.name,
            fontSize = 30.sp,)
    }
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Box(contentAlignment = Alignment.Center) {

            if(state.resting){
                val restTime: Int = 90
                val animationRate: Double = 0.5
                val delayRate: Double = (animationRate / (360/restTime).toDouble())
                var ticks by remember { mutableIntStateOf(0) }

                LaunchedEffect(Unit) {
                    while (360f - animationRate * ticks > 0) {
                        delay(delayRate.seconds)
                        ticks++
                    }
                    onEvent(ExerciseEvent.NextExercise)
                }
                CircularProgressBar(size = 220.dp, angle = (360f - animationRate * ticks).toFloat())
            }

            CircularProgressBar(angle = ((state.set.toFloat()) / 5) * 360f)



            //CustomerCircularProgressBar(state = state, onEvent = onEvent)
            ElevatedButton(
                onClick = { if(state.lastSet){ onEvent(ExerciseEvent.FinishedExercise)} else{onEvent(ExerciseEvent.FinishedSet) }},
                modifier = Modifier.size(200.dp),
                shape = CircleShape
            ) {
                if (state.resting){
                    Text(text = "⧖",
                        fontSize = 60.sp,)
                }else{
                    Text(text = formatWeight(state.weight) + "kg",
                        fontSize = 30.sp,)
                }
            }
            //BottomToTopFilledCircle(progress = 0.5f)
        }

        Row {



            NumberPicker(
                value = state.rep,
                onValueChange = {
                    onEvent(ExerciseEvent.RepChange(it))
                },
                range = 1..5
            )
            Text(text = "Reps",
                modifier =  Modifier.padding(24.dp))
        }
        Row {
            Button(onClick = { /*TODO*/ },
                shape = RoundedCornerShape(size = 10.dp)) {
                Text(text = state.rep.toString())
            }
            Button(onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(size = 10.dp)) {
                Text(text = formatWeight(state.weight))
            }
        }

    }
}



fun formatWeight(float: Float):String{
    return if((float % 1).toFloat() == 0.5f){
        String.format("%.1f", float)
    }else{
        String.format("%.0f", float)
    }
}




@Composable
fun SetProgressBar(
    size: Dp = 240.dp,
    strokeWidth: Dp = 20.dp,
    backgroundArcColor: Color = Color.LightGray,
    backgroundArcColor2: Color = Color.Blue,
    state: ExerciseState,
){
    Canvas(modifier = Modifier.size(size)) {
        drawArc(
            color = backgroundArcColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            size = Size(size.toPx(), size.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
        drawArc(
            color = backgroundArcColor2,
            startAngle = 270f,
            sweepAngle = ((state.set.toFloat()) / 5) * 360f,
            useCenter = false,
            size = Size(size.toPx(), size.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
    }

}

@Composable
fun SmallCircleProgressBar(
    size: Dp = 220.dp,
    strokeWidth: Dp = 20.dp,
    backgroundArcColor: Color = Color.LightGray,
    backgroundArcColor2: Color = Color.Red,
    onEvent: (ExerciseEvent) -> Unit,
    restTime: Int = 90,
    animationRate: Double = 0.5,
    delayRate: Double = (animationRate / (360/restTime).toDouble())
){
    var ticks by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (360f - animationRate * ticks > 0) {
            delay(delayRate.seconds)
            ticks++
        }
        onEvent(ExerciseEvent.NextExercise)
    }
    Canvas(modifier = Modifier.size(size)) {
        drawArc(
            color = backgroundArcColor2,
            startAngle = 270f,
            sweepAngle = (360f - animationRate * ticks).toFloat(),
            useCenter = false,
            size = Size(size.toPx(), size.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
    }
}



@Composable
fun CustomerCircularProgressBar(
    size: Dp = 240.dp,
    strokeWidth: Dp = 20.dp,
    backgroundArcColor: Color = Color.LightGray,
    backgroundArcColor2: Color = Color.Blue,
    state: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit,
    restTime: Int = 90,
    animationRate: Double = 0.5,
    delayRate: Double = (animationRate / (360/restTime).toDouble())
) {
    var ticks by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (360f - animationRate * ticks > 0) {
            delay(delayRate.seconds)
            ticks++
        }
        onEvent(ExerciseEvent.NextExercise)
    }
    Canvas(modifier = Modifier.size(size)) {
        drawArc(
            color = backgroundArcColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            size = Size(size.toPx(), size.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
        drawArc(
            color = backgroundArcColor2,
            startAngle = 270f,
            sweepAngle = (360f - animationRate * ticks).toFloat(),
            useCenter = false,
            size = Size(size.toPx(), size.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
    }
}

