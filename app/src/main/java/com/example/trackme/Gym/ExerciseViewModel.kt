package com.example.trackme.Gym

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackme.Database.Entitis.Exercise
import com.example.trackme.Database.Entitis.ExerciseInRoutine
import com.example.trackme.Database.Entitis.ExerciseInstance
import com.example.trackme.Database.ExerciseDao
import com.example.trackme.Database.Entitis.ExerciseRecord
import com.example.trackme.Database.Entitis.RepRecord
import com.example.trackme.Database.Entitis.Routine
import com.example.trackme.Database.joins.ExerciseJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue

class ExerciseViewModel(
    private val dao: ExerciseDao,
    ): ViewModel() {
    private val _state = MutableStateFlow(ExerciseState())
    val state = _state

    private val routinName:String = "5x5"
    private lateinit var exercises: Queue<ExerciseJoin>
    private lateinit var currentExercise: ExerciseJoin
    private lateinit var routine: Routine

    init {
        viewModelScope.launch {
            routine = dao.getRoutine(routinName)
            exercises = getExercises()
            setExercise()
            state.update { it.copy(
                sessionId = routine.currentSession,
                )
            }
        }
    }

    fun onEvent(event: ExerciseEvent){
        when(event){
            ExerciseEvent.FinishedSet -> {
                viewModelScope.launch {
                    if (state.value.setNum == 1) {
                        insertExerciseRec()
                    }
                    insertSetRec(state.value.setNum, state.value.repsCompleted)
                    if (state.value.setNum == currentExercise.sets) {
                        if (checkCompletion()) {
                            dao.updateRecored(currentExercise.exerciseInstanceId, routine.currentSession)
                        }
                        setExercise()
                    } else {
                        state.update {
                            it.copy(
                                setNum = state.value.setNum.inc(),
                                repsCompleted = currentExercise.reps,
                                resting = true
                            )
                        }
                        timer()
                        animationProgress (time = 90000 ,onValueChange = {percent: Float ->  state.update { it.copy(timerPercent = percent)}})
                        animationProgress(((state.value.setNum - 1).toFloat() / state.value.totalNumSetsTodo) / (state.value.setNum.toFloat() / state.value.totalNumSetsTodo),
                            onValueChange = { percent: Float ->
                                state.update { it.copy(percentage = percent) }
                            }
                        )

                    }
                }
            }
            is ExerciseEvent.RepChange -> {
                _state.update { it.copy(
                        repsCompleted = event.n
                    )
                }
            }
        }
    }

    private suspend fun setExercise(){
        val exercise = exercises.poll()
        if (exercise == null){
            state.update { it.copy(sessionOver = true) }
        }else{
            currentExercise = exercise
            var sets = getSet(currentExercise)
            println(sets)
            if (sets == null){
                sets = 0
            }
            if (sets == currentExercise.sets){
                setExercise()
            }else{
                state.update { it.copy(
                    name = currentExercise.name,
                    weight = getWeight(currentExercise),
                    setNum = sets + 1,
                    repsCompleted = currentExercise.reps,
                    totalNumSetsTodo = currentExercise.sets
                ) }
            }
        }
    }

    private  suspend fun getSet(exercise: ExerciseJoin): Int?{
        return dao.getSetsDone(exercise.exerciseInstanceId, routine.currentSession)
    }

    private suspend fun getWeight(exercise: ExerciseJoin): Float{
        var weight = exercise.startingWeight
        val last3Rec = dao.getLast3Rec(exercise.exerciseInstanceId)
        if (last3Rec.isNotEmpty()) {
            if (last3Rec[0].complete) {
                weight = last3Rec[0].weight + 25
            } else if (checkDeLoud(last3Rec)) {
                weight = deLoudCalculator(last3Rec[0].weight)
            } else {
                weight = last3Rec[0].weight
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

    private suspend fun checkCompletion():Boolean{
        return dao.checkCompletion(currentExercise.exerciseInstanceId, routine.currentSession) == currentExercise.sets * 5
    }

    private fun checkDeLoud(exercises: List<ExerciseRecord>): Boolean {
        println(exercises.size == 3 && !exercises[1].complete && !exercises[2].complete)
        return (exercises.size == 3 && !exercises[1].complete && !exercises[2].complete)
    }

    private suspend fun insertExerciseRec(){
        dao.insertExerciseRecord(ExerciseRecord(sessionId = routine.currentSession, exerciseInstanceId = currentExercise.exerciseInstanceId, weight = (state.value.weight*10).toInt()))
    }

    private suspend fun insertSetRec(setNum: Int, repsDone: Int){
        dao.insertRepRecord(RepRecord(setNum = setNum,  sessionId = routine.currentSession, exerciseInstanceId = currentExercise.exerciseInstanceId, repComp = repsDone))
    }

    private suspend fun getExercises(): LinkedList<ExerciseJoin>{
        return LinkedList(dao.getExercise(routine.id, (routine.currentSession+1)%2))
    }

    private fun timer(){
        object : CountDownTimer(90000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                state.update { it.copy(restTimerCoutdown = millisUntilFinished) }
            }
            override fun onFinish() {
                _state.update { it.copy(
                    resting = false
                )}
            }
        }.start()
    }

    private fun animationProgress(start: Float = 0f, time: Long = 1000, onValueChange: (percent:Float)-> Unit){
        val valueHolder = PropertyValuesHolder.ofFloat(
            "angle",
            start,
            1f
        )
        val animator = ValueAnimator().apply {
            setValues(valueHolder)
            duration = time
            interpolator = null

            addUpdateListener {
                val percentage = it.getAnimatedValue("angle") as Float
                onValueChange(percentage)
            }
        }
        animator.start()
    }
//    private suspend fun insertDummyData(){
//        dao.insertRoutine(Routine(currentSession = 1, name = "5x5"))
//
//        dao.insertExercise(Exercise(name = "Squat", type = "compound", equipment = "barbell", musclesTargeted = "Legs"))
//        dao.insertExercise(Exercise(name = "Bench Press", type = "compound", equipment = "barbell", musclesTargeted = "Chest"))
//        dao.insertExercise(Exercise(name = "Overhead Press", type = "compound", equipment = "barbell", musclesTargeted = "Shoulders"))
//        dao.insertExercise(Exercise(name = "Deadlift", type = "compound", equipment = "barbell", musclesTargeted = "Lower Back"))
//        dao.insertExercise(Exercise(name = "Barbell Row", type = "compound", equipment = "barbell", musclesTargeted = "Upper Back"))
//
//        dao.insertExerciseInstance(ExerciseInstance(exerciseId = "Squat".hashCode(), reps = 5, sets = 5, startingWeight = 500 ))
//        dao.insertExerciseInstance(ExerciseInstance(exerciseId = "Bench Press".hashCode(), reps = 5, sets = 5, startingWeight = 350 ))
//        dao.insertExerciseInstance(ExerciseInstance(exerciseId = "Overhead Press".hashCode(), reps = 5, sets = 5, startingWeight = 200 ))
//        dao.insertExerciseInstance(ExerciseInstance(exerciseId = "Deadlift".hashCode(), reps = 5, sets = 5, startingWeight = 800 ))
//        dao.insertExerciseInstance(ExerciseInstance(exerciseId = "Barbell Row".hashCode(), reps = 5, sets = 5, startingWeight = 300 ))
//
//        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseInstanceId = 1, day = 0, exerciseOrder = 0))
//        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseInstanceId = 2, day = 0, exerciseOrder = 1))
//        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseInstanceId = 5, day = 0, exerciseOrder = 2))
//
//        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseInstanceId = 1, day = 1, exerciseOrder = 0))
//        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseInstanceId = 3, day = 1, exerciseOrder = 1))
//        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseInstanceId = 4, day = 1, exerciseOrder = 2))
//    }
}