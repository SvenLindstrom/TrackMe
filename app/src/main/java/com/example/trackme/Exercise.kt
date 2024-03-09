package com.example.trackme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise (
    @PrimaryKey(autoGenerate = false)
    val name: String = "Squat",
    val weight: Float = 50f
)