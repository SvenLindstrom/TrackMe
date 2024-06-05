package com.example.trackme

import com.example.trackme.Database.Entitis.ExerciseRecord

data class EndState(
    val exerciseRecords: List<ExerciseRecord> = listOf(),
    val sessionId: Int = 0,
    val exerciseNames: List<String> = listOf()
)