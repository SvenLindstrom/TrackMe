package com.example.trackme.Gym

import androidx.lifecycle.viewModelScope
import com.example.trackme.Database.Entitis.ExerciseRecord
import com.example.trackme.Database.Entitis.RepRecord
import com.example.trackme.Database.Entitis.Routine
import com.example.trackme.Database.ExerciseDao
import com.example.trackme.Database.joins.ExerciseJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.LinkedList

class SessionManager(
    private val dao: ExerciseDao,
    private val routine: Routine,
) {

    val exercises: MutableList<SessionExercise> = mutableListOf()
    var nextExerciseIndex: Int = 0


    fun getNextExercise():SessionExercise?{

        if(nextExerciseIndex == exercises.size){
            return null
        }
        val nextExercise = exercises[nextExerciseIndex]
        nextExerciseIndex++
        return nextExercise
    }

     suspend fun getExercises() {
        val daoExercises = dao.getExercise(routine.id, (routine.currentSession+1)%2)
        daoExercises.forEach {
            exercises.add(
                SessionExercise(
                    name = it.name,
                    setNum = it.sets,
                    sets = mutableListOf(),
                    reps = it.reps,
                    weight = getWeight(it),
                    instanceId = it.exerciseInstanceId,
                )
            )
        }
    }

    suspend fun saveSession(){
        exercises.forEach {
            insertExerciseRec(it)
            it.sets.forEachIndexed { index, i -> insertSetRec(index, i, it) }
        }
    }

    private suspend fun insertExerciseRec(exercise: SessionExercise){
        dao.insertExerciseRecord(ExerciseRecord(sessionId = routine.currentSession, exerciseInstanceId = exercise.instanceId, weight = (exercise.weight*10).toInt(), complete = exercise.sets.all { it == exercise.reps }))
    }

    private suspend fun insertSetRec(setNum: Int, repsDone: Int, exercise: SessionExercise){
        dao.insertRepRecord(RepRecord(setNum = setNum,  sessionId = routine.currentSession, exerciseInstanceId = exercise.instanceId, repComp = repsDone))
    }

    private suspend fun getWeight(exercise: ExerciseJoin): Float{
        var weight = exercise.startingWeight
        val last3Rec = dao.getLast3Rec(exercise.exerciseInstanceId)
        if (last3Rec.isNotEmpty()) {
            weight = if (last3Rec[0].complete) {
                last3Rec[0].weight + 25
            } else if (checkDeLoud(last3Rec)) {
                deLoudCalculator(last3Rec[0].weight)
            } else {
                last3Rec[0].weight
            }
        }
        return weight/10f
    }

    private fun deLoudCalculator(oldWeight: Int): Int{
        var num = ((oldWeight) * 0.9).toInt()
        val temp = (num % 25)
        if (temp > 13){
            num += (25 - temp)
        }else{
            num -= temp
        }
        return num
    }

    private fun checkDeLoud(exercises: List<ExerciseRecord>): Boolean {
        println(exercises.size == 3 && !exercises[1].complete && !exercises[2].complete)
        return (exercises.size == 3 && !exercises[1].complete && !exercises[2].complete)
    }
}