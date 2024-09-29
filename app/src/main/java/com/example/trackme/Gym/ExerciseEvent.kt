package com.example.trackme.Gym

sealed interface ExerciseEvent {
    data object FinishedSet: ExerciseEvent
    data class RepChange(val n:Int): ExerciseEvent
    data object SkipRest: ExerciseEvent
    data class UpdateWeight(val n:String): ExerciseEvent
}