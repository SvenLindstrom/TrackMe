package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = ExerciseRecord::class,
        parentColumns = arrayOf("sessionId","exerciseInstanceId"),
        childColumns = arrayOf("sessionId","exerciseInstanceId"),
        onDelete = ForeignKey.CASCADE
    )
))
data class RepRecord (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val setNum: Int,
    val sessionId: Int,
    val exerciseInstanceId: Int,
    val repComp: Int
    )
