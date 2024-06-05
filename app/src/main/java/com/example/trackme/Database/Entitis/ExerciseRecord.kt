package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = arrayOf("sessionId","exerciseId"))
data class ExerciseRecord(
    val sessionId: Int,
    val exerciseId: Int,
    val weight: Int,
    val complete: Boolean = false
)

