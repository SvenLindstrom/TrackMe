package com.example.trackme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepRecord (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val exerciseId: Int,
    val setNum: Int,
    val repNum: Int,
    )
