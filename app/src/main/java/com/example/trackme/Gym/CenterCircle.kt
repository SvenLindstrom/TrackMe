package com.example.trackme.Gym

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackme.UiElements.CircularProgressBar

@Composable
fun CenterCircle(state: ExerciseState, onEvent:  (ExerciseEvent) -> Unit){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
    ) {

        if (state.resting) {
            RestTimer(state, onEvent)
        } else {
            CompleteSet(onEvent = onEvent, state = state)
        }
        CircularProgressBar(
            angle = (state.setNum.toFloat() / state.totalNumSetsTodo) * 360f,
            animator = state.percentage
        )
    }
}