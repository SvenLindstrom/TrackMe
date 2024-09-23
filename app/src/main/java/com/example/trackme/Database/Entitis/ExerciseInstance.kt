package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(
        entity = Exercise::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("exerciseId")
    ),
))
data class ExerciseInstance (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int,
    val sets: Int,
    val reps: Int,
    val startingWeight: Int,
)