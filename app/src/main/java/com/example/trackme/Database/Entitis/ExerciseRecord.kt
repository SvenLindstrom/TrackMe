package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey



@Entity(primaryKeys = arrayOf("sessionId","exerciseInstanceId"),foreignKeys = arrayOf(
    ForeignKey(
        entity = ExerciseInstance::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("exerciseInstanceId")
    ))
)
data class ExerciseRecord(
    val sessionId: Int,
    val exerciseInstanceId: Int,
    val weight: Int,
    val complete: Boolean = false
)

