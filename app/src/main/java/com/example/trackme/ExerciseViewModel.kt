package com.example.trackme

import android.view.contentcapture.ContentCaptureSessionId
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseViewModel(
    private val dao: ExerciseDao,

): ViewModel() {
    private val _state = MutableStateFlow(ExerciseState())
    val state = _state

    private val _exercise = MutableLiveData<Exercise>()
    private val exercise: LiveData<Exercise> = _exercise
    private var exerciseList = exerciseList()
    private var exerciseNum = 0
    private var upWeight = true
    init {
        viewModelScope.launch {
            val exerciseId = fetchExerciseID()
            val sessionId = fetchSessionID()
            exerciseList = exerciseList(sessionId = sessionId)
            val initialExercise = fetchExercise(numStrTranslator(exerciseList[exerciseNum]))

            _exercise.value = initialExercise
            _state.update { it.copy(
                sessionId = sessionId,
                exerciseId = exerciseId,
                name = initialExercise.name,
                weight = initialExercise.weight
            )
                }
            }
        }

    fun onEvent(event: ExerciseEvent){
        when(event){
            ExerciseEvent.FinishedExercise -> {
                var sessionId = state.value.sessionId
                val name = state.value.name
                val weight = state.value.weight
                val exerciseRecord = ExerciseRecord(sessionId = sessionId, name = name, weight = weight)

                if (upWeight){ upDateExerciseWeight() }
                upWeight = true

                if (exerciseNum == 2){
                    exerciseNum = 0
                    sessionId = sessionId.plus(1)
                    exerciseList = exerciseList()
                }else{
                    exerciseNum++
                }

                viewModelScope.launch {
                    dao.insertExerciseRecord(exerciseRecord)
                    dao.updateExercise(exercise.value!!)
                    val nextExercise = fetchExercise(numStrTranslator(exerciseList[exerciseNum]))

                    _state.update {
                        it.copy(
                            weight = nextExercise.weight,
                            lastSet = false,
                            set = 1,
                            name = nextExercise.name,
                            sessionId = sessionId,
                            exerciseId = _state.value.exerciseId.inc()
                        )
                    }
                }
            }
            ExerciseEvent.FinishedSet -> {
                var lastSet = false
                if(state.value.rep != 5){
                    upWeight = false
                }
                if (state.value.set == 4){
                    lastSet = true
                }

                val repRecord = RepRecord(
                    exerciseId = state.value.exerciseId,
                    setNum = state.value.set,
                    repNum = state.value.rep
                )

                viewModelScope.launch {
                    dao.insertRepRecord(repRecord)
                }

                _state.update { it.copy(
                    set = state.value.set.inc(),
                    lastSet = lastSet,
                    rep = 5,
                    resting = true
                    )
                }
            }

            ExerciseEvent.DecRep -> {
                var newRep = state.value.rep
                if(state.value.rep > 0){
                    newRep = state.value.rep.dec()
                }
                _state.update { it.copy(
                    rep = newRep
                ) }
            }
            ExerciseEvent.IncRep -> {
                var newRep = state.value.rep
                if(state.value.rep < 5){
                    newRep = state.value.rep.inc()
                }
                _state.update { it.copy(
                        rep = newRep
                    )
                }
            }

            is ExerciseEvent.RepChange -> {
                _state.update { it.copy(
                        rep = event.n
                    )
                }
            }

            ExerciseEvent.NextExercise -> {
                    _state.update { it.copy(
                        resting = false
                    )
                }
            }
        }
    }

    private fun upDateExerciseWeight(){
        viewModelScope.launch {
            val newExercise = Exercise(
                name = _exercise.value!!.name,
                weight = _exercise.value!!.weight.plus(2.5f)
            )
            dao.updateExercise(newExercise)
        }
    }

    private suspend fun fetchExercise(name:String):Exercise{
        return withContext(Dispatchers.IO){
            dao.getExercise(name)
        }
    }

    private  suspend fun fetchExerciseID():Int{
        return withContext(Dispatchers.IO){
            dao.getExerciseId() + 1
        }
    }

    private  suspend fun fetchSessionID():Int{
        return withContext(Dispatchers.IO){
            dao.getSessionId() + 1
        }
    }

    private fun numStrTranslator(n:Int): String{
        return when(n){
            0 -> "Squat"
            1 -> "Bench Press"
            2 -> "Row"
            3 -> "Overhead"
            4 -> "DeadLift"
            else -> {return "test"}
        }
    }

    private fun exerciseList(
        sessionId: Int = state.value.sessionId
    ): List<Int>{
        return if (sessionId % 2 == 0){
                listOf(0, 1, 2)
            }else{
                listOf(0, 3, 4)
            }
        }
}