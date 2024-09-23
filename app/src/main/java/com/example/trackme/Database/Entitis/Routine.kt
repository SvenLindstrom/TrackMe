package com.example.trackme.Database.Entitis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Routine (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val currentSession: Int
)