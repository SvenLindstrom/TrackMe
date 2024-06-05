package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name: String = "Squat",
    val sets: Int,
    val reps: Int,
)