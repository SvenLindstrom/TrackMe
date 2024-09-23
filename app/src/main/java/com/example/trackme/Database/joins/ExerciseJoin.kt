package com.example.trackme.Database.joins

data class ExerciseJoin (
    val name: String,
    val sets: Int,
    val reps: Int,
    val startingWeight: Int,
    val exerciseInstanceId: Int,
)