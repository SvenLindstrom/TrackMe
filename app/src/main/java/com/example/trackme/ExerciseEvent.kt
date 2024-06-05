package com.example.trackme

sealed interface ExerciseEvent {
    object FinishedExercise: ExerciseEvent
    object FinishedSet: ExerciseEvent
    data class RepChange(val n:Int): ExerciseEvent
}