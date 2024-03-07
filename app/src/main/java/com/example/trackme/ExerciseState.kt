package com.example.trackme

data class ExerciseState(
    val sessionId: Int = 1,
    val name: String = "Squat",
    val weight: Double = 10.25,
    val set: Int = 1,
    val rep: Int = 0,
    val lastSet:Boolean = false,
    val resting: Boolean = false
)

