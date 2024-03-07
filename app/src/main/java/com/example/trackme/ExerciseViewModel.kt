package com.example.trackme

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

    val _weights = MutableLiveData<Weights>(Weights())
    val weights: LiveData<Weights> = _weights

    private var exerciseList = exerciseList()
    private var exerciseNum = 0
    private var upWeight = true
    private val _state = MutableStateFlow(ExerciseState())
    val state = _state
    init {
        viewModelScope.launch {
            val initialWeights = fetchInitialWeights()
            _weights.value = initialWeights
            exerciseList = exerciseList()
            _state.update {it.copy(
                name = numStrTranslator(exerciseList[exerciseNum]),
                weight = getWeight(),
                sessionId = weights.value!!.seassionNum,
            )
            }
        }
    }
    fun onEvent(event: ExerciseEvent){
        when(event){
            ExerciseEvent.FinishedExercise -> {
                val sessionId = weights.value!!.seassionNum
                val name = state.value.name
                val weight = state.value.weight
                val exercise = Exercise(sessionId = sessionId, name = name, weight = weight)

                if (upWeight){ upDateWeight() }
                upWeight = true

                if (exerciseNum == 2){
                    exerciseNum = 0
                    _weights.value!!.seassionNum = _weights.value!!.seassionNum.plus(1)
                    exerciseList = exerciseList()
                }else{
                    exerciseNum++
                }

                viewModelScope.launch {
                    dao.insertExercise(exercise)
                    dao.updateWeights(weights.value!!)
                }

                _state.update { it.copy(
                    weight = getWeight(),
                    lastSet = false,
                    set = 1,
                    name = numStrTranslator(exerciseList[exerciseNum]),
                    sessionId = _weights.value!!.seassionNum,

                ) }

            }
            ExerciseEvent.FinishedSet -> {
                var lastSet = false
                if(state.value.rep != 5){
                    upWeight = false
                }
                if (state.value.set == 4){
                    lastSet = true
                }
                _state.update { it.copy(
                    set = state.value.set.inc(),
                    lastSet = lastSet,
                    rep = 5,
                    resting = true
                ) }
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
                ) }
            }

            is ExerciseEvent.RepChange -> {
                _state.update { it.copy(
                    rep = event.n
                ) }
            }

            ExerciseEvent.NextExercise -> {
                _state.update { it.copy(
                    resting = false
                ) }
            }
        }
    }



    fun getWeight(): Double{
        val n = exerciseList[exerciseNum]
        return when(n){
            0 -> weights.value!!.squat
            1 -> weights.value!!.benchPress
            2 -> weights.value!!.rows
            3 -> weights.value!!.overHeadPress
            4 -> weights.value!!.deadLift
            else -> {_weights.value!!.squat}
        }


    }

    fun upDateWeight(){
        val n = exerciseList[exerciseNum]
         when(n){
            0 -> _weights.value!!.squat = _weights.value!!.squat.plus(2.5)
            1 -> _weights.value!!.benchPress = _weights.value!!.benchPress.plus(2.5)
            2 -> _weights.value!!.rows = _weights.value!!.rows.plus(2.5)
            3 -> _weights.value!!.overHeadPress = _weights.value!!.overHeadPress.plus(2.5)
            4 -> _weights.value!!.deadLift = _weights.value!!.deadLift.plus(2.5)
            else -> {_weights.value!!.squat}
        }
    }

    private suspend fun fetchInitialWeights(): Weights {
        return withContext(Dispatchers.IO) {
            dao.getCurrentWeights()
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


    private fun exerciseList(): List<Int>{

        val weightMap = if (_weights.value!!.seassionNum % 2 == 0){
            listOf(0, 1, 2)
        }else{
            listOf(0, 3, 4)
        }
        return weightMap
    }


}