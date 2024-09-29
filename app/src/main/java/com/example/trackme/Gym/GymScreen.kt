package com.example.trackme.Gym

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackme.UiElements.CircularProgressBar
import com.example.trackme.UiElements.NumberPicker
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

    val showUpdate = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row {
                Text(
                    text = "#" + state.sessionId, modifier = Modifier
                        .padding(5.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { showUpdate.value = true },
                    ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                }
            }
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = state.name,
                fontSize = 50.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 5.dp)
                    .align(Alignment.Start)
            )
            //Spacer(modifier = Modifier.weight(1f))
            CenterCircle(state, onEvent)
            Spacer(modifier = Modifier.weight(0.4f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                RepsDisplayBox(state, onEvent)
                WeightDisplayBox(buttonContend = state.weight.toString())
            }
            Spacer(modifier = Modifier.weight(0.4f))
        }
    }
    if(showUpdate.value) {
        Column(modifier = Modifier.fillMaxSize()) {
            val text = remember { mutableStateOf("") }
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
            )
            Row {
                Button(
                    onClick = {
                        onEvent(ExerciseEvent.UpdateWeight(text.value))
                        showUpdate.value = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                }
                Button(
                    onClick = {
                        showUpdate.value = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        }
    }
}



@Composable
fun CompleteSet(onEvent: (ExerciseEvent) -> Unit, state: ExerciseState){
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
fun RepsDisplayBox(state: ExerciseState, onEvent: (ExerciseEvent) -> Unit){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
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
        Text(
            text = "Reps",
            modifier = Modifier.padding(end = 80.dp, bottom = 80.dp)
        )
    }
}

@Composable
fun WeightDisplayBox(buttonContend: String) {
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







