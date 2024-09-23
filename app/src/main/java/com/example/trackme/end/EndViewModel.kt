package com.example.trackme.end

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackme.Database.Entitis.Routine
import com.example.trackme.Database.ExerciseDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EndViewModel(
    private val dao: ExerciseDao,
): ViewModel() {
    private val _state = MutableStateFlow(EndState())
    val state = _state
    private lateinit var routin: Routine

    init {
        viewModelScope.launch {
            routin = dao.getRoutine("5x5")
            val exercises =  dao.getExerciseRecForSession(routin.currentSession)
            state.update { it.copy(
                sessionId = routin.currentSession,
                exerciseRecords = exercises,
            ) }
        }
    }

    fun onEvent(event: EndEvents) {
        when (event) {
            EndEvents.FishSession -> {
                viewModelScope.launch {
                dao.updateRoutine(routin.copy(currentSession = routin.currentSession.inc()))
            }
            }
        }
    }
}