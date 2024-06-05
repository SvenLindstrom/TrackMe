package com.example.trackme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackme.UiElements.CircularProgressBar

@Composable
fun end(
    navController: NavController,
    onEvent: (ExerciseEvent) -> Unit,
    state: EndState
){
//    Box(modifier = Modifier, contentAlignment = Alignment.Center){
//        CircularProgressBar(angle = 0f)
//        Button(
//            onClick = { navController.popBackStack("start", inclusive = false) },
//            modifier = Modifier.size(220.dp),
//            shape = CircleShape,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.background,
//                contentColor = Color.White
//            )
//        ) {
//            Text(
//                text = """Ended""".trimMargin(),
//                fontSize = 40.sp,
//            )
//        }
//    }
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Session #" + state.sessionId.toString())
            for (i in state.exerciseRecords.indices) {
                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    Text(
                        text = state.exerciseNames[i],
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
//                    Text(
//                        text = state.exerciseRecords[i].complete.toString(),
//                        modifier = Modifier.weight(1f),
//                        textAlign = TextAlign.Center
//                    )
                    Text(
                        text = (state.exerciseRecords[i].weight / 10f).toString() + "kg",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Divider(
                        modifier = Modifier.weight(7f),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Row {
                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = {
                    navController.popBackStack("start", inclusive = false) },

                ) {
                    Text(text = "End Session")
                    //Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "")
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "")
                }
            }
        }
    }
}