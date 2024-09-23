package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise (
    val name: String = "Squat",
    @PrimaryKey
    val id: Int = name.hashCode(),
    val type: String,
    val equipment: String,
    val musclesTargeted: String,
)