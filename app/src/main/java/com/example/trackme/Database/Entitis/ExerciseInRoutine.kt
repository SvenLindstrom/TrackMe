package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = arrayOf("routineId", "exerciseId", "day"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Routine::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("routineId")),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("exerciseId"))
    )
)
data class ExerciseInRoutine (
    val routineId: Int,
    val exerciseId: Int,
    val day: Int,
    val exerciseOrder: Int
)