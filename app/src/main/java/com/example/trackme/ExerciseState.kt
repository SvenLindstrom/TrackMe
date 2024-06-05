package com.example.trackme

data class ExerciseState(
    val sessionId: Int = 1,
    val name: String = "Squat",
    val weight: Float = 550f,
    val setNum: Int =  1,
    val repsCompleted: Int = 5,
    val resting: Boolean = false,
    val totalNumSetsTodo: Int = 5,
    val percentage: Float = 1f,
    val timerPercent: Float = 0f,
    val sessionOver: Boolean = false,
    val restTimerCoutdown: Long = 0,
    val circelLable: String = """Finish
                        |
                        |  Set"""
)

