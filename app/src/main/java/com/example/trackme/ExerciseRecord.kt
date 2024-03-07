package com.example.trackme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExerciseRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val sessionId: Int,
    val name: String,
    val weight: Float,
)

