package com.example.trackme.end

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun End(
    navController: NavController,
    onEvent: (EndEvents) -> Unit,
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
            for (record in state.exerciseRecords) {
                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    Text(
                        text = record.name,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
//                    Text(
//                        text = state.exerciseRecords[i].complete.toString(),
//                        modifier = Modifier.weight(1f),
//                        textAlign = TextAlign.Center
//                    )
                    Text(
                        text = (record.weight / 10f).toString() + "kg",
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
            Row(Modifier.align(Alignment.End)) {
                Button(onClick = {
                    onEvent(EndEvents.FishSession)
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