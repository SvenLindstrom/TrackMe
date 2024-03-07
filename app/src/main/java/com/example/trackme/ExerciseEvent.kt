package com.example.trackme

sealed interface ExerciseEvent {
    object FinishedExercise: ExerciseEvent
    object FinishedSet: ExerciseEvent
    object IncRep: ExerciseEvent
    object DecRep: ExerciseEvent
    data class RepChange(val n:Int): ExerciseEvent
    object NextExercise: ExerciseEvent
}