package com.example.trackme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weights (
    @PrimaryKey
    val current: Int = 1,
    var seassionNum: Int = 1,
    var squat: Double = 50.0,
    var benchPress: Double = 30.0,
    var overHeadPress: Double = 25.0,
    var rows: Double = 60.0,
    var deadLift: Double = 90.0
)