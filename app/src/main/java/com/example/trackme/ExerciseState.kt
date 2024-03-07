package com.example.trackme

data class ExerciseState(
    val sessionId: Int = 1,
    val exerciseId: Int = 1,
    val name: String = "Squat",
    val weight: Float = 10.25f,
    val set: Int = 1,
    val rep: Int = 0,
    val lastSet:Boolean = false,
    val resting: Boolean = false
)

