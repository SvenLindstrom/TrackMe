package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = ExerciseRecord::class,
        parentColumns = arrayOf("sessionId","exerciseId"),
        childColumns = arrayOf("sessionId","exerciseId"),
        onDelete = ForeignKey.CASCADE
    )
))
data class RepRecord (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val setNum: Int,
    val sessionId: Int,
    val exerciseId: Int,
    val repComp: Int
    )
