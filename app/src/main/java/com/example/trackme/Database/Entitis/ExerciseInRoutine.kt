package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = arrayOf("routineId", "exerciseInstanceId", "day"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Routine::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("routineId")),
        ForeignKey(
            entity = ExerciseInstance::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("exerciseInstanceId"))
    )
)
data class ExerciseInRoutine (
    val routineId: Int,
    val exerciseInstanceId: Int,
    val day: Int,
    val exerciseOrder: Int
)