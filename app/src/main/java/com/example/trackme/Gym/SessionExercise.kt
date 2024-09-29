package com.example.trackme.Gym

data class SessionExercise(
    val name: String,
    val setNum: Int,
    val sets: MutableList<Int>,
    val reps: Int,
    var weight: Float,
    val instanceId: Int,
){
    fun updateSet(setCompleted: Int){
        sets.add(setCompleted)
    }
}