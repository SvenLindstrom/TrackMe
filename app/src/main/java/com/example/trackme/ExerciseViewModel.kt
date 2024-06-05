package com.example.trackme

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.trackme.Database.Entitis.Exercise
import com.example.trackme.Database.Entitis.ExerciseInRoutine
import com.example.trackme.Database.ExerciseDao
import com.example.trackme.Database.Entitis.ExerciseRecord
import com.example.trackme.Database.Entitis.RepRecord
import com.example.trackme.Database.Entitis.Routine
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
    private lateinit var exercises: Queue<Exercise>
    private lateinit var currentExercise: Exercise
    private lateinit var routine: Routine

    init {
        viewModelScope.launch {
            val routineTemp = dao.getRoutine(routinName)
            if (routineTemp == null){
                println("AAAAAAAAAAAAAAAAAAAAAAA")
                insertDummyData()
                routine = dao.getRoutine(routinName)
            }else{
                println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                routine = routineTemp
            }
            setUpSession()
        }
    }

    fun onEvent(event: ExerciseEvent){
        when(event){
            ExerciseEvent.FinishedExercise -> {
//                var sessionId = state.value.sessionId
//                val name = state.value.name
//                val weight = state.value.weight
//                val exerciseRecord = ExerciseRecord(sessionId = sessionId, name = name, weight = weight)
//
//                if (upWeight){ upDateExerciseWeight() }
//                upWeight = true
//
//                if (exerciseNum == 2){
//                    exerciseNum = 0
//                    sessionId = sessionId.plus(1)
//                    exerciseList = exerciseList()
//                }else{
//                    exerciseNum++
//                }
//
//                viewModelScope.launch {
//                    dao.insertExerciseRecord(exerciseRecord)
//                    dao.updateExercise(exercise.value!!)
//                    val nextExercise = fetchExercise(numStrTranslator(exerciseList[exerciseNum]))
//
//                    _state.update {
//                        it.copy(
//                            weight = nextExercise.weight,
//                            lastSet = false,
//                            set = 1,
//                            name = nextExercise.name,
//                            sessionId = sessionId,
//                            exerciseId = _state.value.exerciseId.inc()
//                        )
//                    }
//                }
            }
            ExerciseEvent.FinishedSet -> {
                viewModelScope.launch {
                    if (state.value.setNum == 1) {
                        insertExerciseRec()
                    }
                    insertSetRec(state.value.setNum, state.value.repsCompleted)

                    if (state.value.setNum == currentExercise.sets) {
                        if (checkCompletion()) {
                            dao.updateRecored(currentExercise.id, routine.currentSession)
                        }
                        setExercise()
                    } else {
                        state.update {
                            it.copy(
                                setNum = state.value.setNum.inc(),
                                repsCompleted = currentExercise.reps,
                                //resting = true
                            )
                        }
                        //timer()
                        //animationProgress (durationn = 90000 ,onValueChange = {percent: Float ->  state.update { it.copy(timerPercent = percent)}})
                        animationProgress(((state.value.setNum - 1).toFloat() / state.value.totalNumSetsTodo) / (state.value.setNum.toFloat() / state.value.totalNumSetsTodo),
                            onValueChange = { percent: Float ->
                                state.update { it.copy(percentage = percent) }
                            }
                        )

                    }

                    //println(state.value.setNum)
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


    // move to innit
    private fun setUpSession(){
        viewModelScope.launch {
            routine = dao.getRoutine(routinName)
            exercises = getExercises()
            setExercise()
            state.update { it.copy(
                sessionId = routine.currentSession,
                sessionOver = false)
            }
        }
    }

    private suspend fun setExercise(){
        val exercise = exercises.poll()
        if (exercise == null){
            state.update { it.copy(sessionOver = true) }
        }else{
            currentExercise = exercise
            state.update { it.copy(
                name = currentExercise.name,
                weight = getWeight(currentExercise),
                setNum = 1,
                repsCompleted = currentExercise.reps,
                totalNumSetsTodo = currentExercise.sets
            ) }
        }
    }

    private suspend fun getWeight(exercise: Exercise): Float{
            var weight = 500
            val last3Rec = dao.getLast3Rec(exercise.id)
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
        return dao.checkCompletion(currentExercise.id, routine.currentSession) == currentExercise.sets * 5
    }
    private fun checkDeLoud(exercises: List<ExerciseRecord>): Boolean {
        println(exercises.size == 3 && !exercises[1].complete && !exercises[2].complete)
        return (exercises.size == 3 && !exercises[1].complete && !exercises[2].complete)
    }

    private suspend fun insertExerciseRec(){
        dao.insertExerciseRecord(ExerciseRecord(sessionId = routine.currentSession, exerciseId = currentExercise.id, weight = (state.value.weight*10).toInt()))
    }

    private suspend fun insertSetRec(setNum: Int, repsDone: Int){
        dao.insertRepRecord(RepRecord(setNum = setNum,  sessionId = routine.currentSession, exerciseId = currentExercise.id, repComp = repsDone))
    }

    private suspend fun getExercises(): LinkedList<Exercise>{
        return LinkedList(dao.getExercise(routine.id, routine.currentSession%2))
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

    private fun animationProgress(start: Float = 0f, durationn: Long = 1000, onValueChange: (percent:Float)-> Unit){
        val valueholder = PropertyValuesHolder.ofFloat(
            "angle",
            start,
            1f
        )
        val animator = ValueAnimator().apply {
            setValues(valueholder)
            duration = durationn
            interpolator = null

            addUpdateListener {
                val percentage = it.getAnimatedValue("angle") as Float
                onValueChange(percentage)
            }
        }
        animator.start()
    }
    private suspend fun insertDummyData(){
        dao.insertRoutine(Routine(currentSession = 4, name = "5x5"))

        dao.insertExercise(Exercise(name = "Squat", reps = 5, sets = 5))
        dao.insertExercise(Exercise(name = "Bench Press", reps = 5, sets = 5))
        dao.insertExercise(Exercise(name = "Overhead Press", reps = 5, sets = 5))
        dao.insertExercise(Exercise(name = "Deadlift", reps = 5, sets = 1))
        dao.insertExercise(Exercise(name = "Barbell Row", reps = 5, sets = 5))

        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseId = 1, day = 0, exerciseOrder = 0))
        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseId = 2, day = 0, exerciseOrder = 1))
        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseId = 5, day = 0, exerciseOrder = 2))

        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseId = 1, day = 1, exerciseOrder = 0))
        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseId = 3, day = 1, exerciseOrder = 1))
        dao.insertExerciseInRoutine(ExerciseInRoutine(routineId = 1, exerciseId = 4, day = 1, exerciseOrder = 2))

        dao.insertExerciseRecord(ExerciseRecord(1,1,500,false))
        dao.insertExerciseRecord(ExerciseRecord(2,1,500,false))
        dao.insertExerciseRecord(ExerciseRecord(3,1,500,false))
    }
}