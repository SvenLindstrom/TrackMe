package com.example.trackme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weights (
    @PrimaryKey
    val current: Int = 1,
    var seassionNum: Int = 1,
    var squat: Float = 50.0f,
    var benchPress: Float = 30.0f,
    var overHeadPress: Float = 25.0f,
    var rows: Float = 60.0f,
    var deadLift: Float = 90.0f
)