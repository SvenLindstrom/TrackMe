package com.example.trackme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise (
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val weight: Float
)