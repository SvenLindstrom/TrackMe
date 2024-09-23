package com.example.trackme.end

import com.example.trackme.Database.joins.RecoredJoin

data class EndState(
    val exerciseRecords: List<RecoredJoin> = listOf(),
    val sessionId: Int = 0,
)