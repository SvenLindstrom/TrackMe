package com.example.trackme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.trackme.Database.Entitis.Exercise
import com.example.trackme.Database.Entitis.ExerciseInRoutine
import com.example.trackme.Database.Entitis.ExerciseInstance
import com.example.trackme.Database.Entitis.Routine
import com.example.trackme.Database.ExerciseDao
import com.example.trackme.Database.ExerciseDatabase
import com.example.trackme.Gym.ExerciseViewModel
import com.example.trackme.end.EndViewModel
import com.example.trackme.ui.theme.TrackMeTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ExerciseDatabase::class.java,
            "gym.db"
        ).build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inserData(db.dao)
        setContent {
            TrackMeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Nav(
                        object : ViewModelProvider.Factory{
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return ExerciseViewModel(db.dao) as T
                            }
                        },
                        object : ViewModelProvider.Factory{
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return EndViewModel(db.dao) as T
                            }
                        }
                    )
                }
            }
        }
    }
}


private fun inserData(dao: ExerciseDao){

    runBlocking {
        launch {
            dao.insertRoutine(Routine(currentSession = 1, name = "5x5"))

            exercieCreation(dao)

            instanceCreation(dao)

            addExerciseToRoutine(dao)
        }
    }
}


private suspend fun exercieCreation(dao: ExerciseDao){
    dao.insertExercise(
        Exercise(
            name = "Squat",
            type = "compound",
            equipment = "barbell",
            musclesTargeted = "Legs"
        )
    )
    dao.insertExercise(
        Exercise(
            name = "Bench Press",
            type = "compound",
            equipment = "barbell",
            musclesTargeted = "Chest"
        )
    )
    dao.insertExercise(
        Exercise(
            name = "Overhead Press",
            type = "compound",
            equipment = "barbell",
            musclesTargeted = "Shoulders"
        )
    )
    dao.insertExercise(
        Exercise(
            name = "Deadlift",
            type = "compound",
            equipment = "barbell",
            musclesTargeted = "Lower Back"
        )
    )
    dao.insertExercise(
        Exercise(
            name = "Barbell Row",
            type = "compound",
            equipment = "barbell",
            musclesTargeted = "Upper Back"
        )
    )
}

private suspend fun instanceCreation(dao: ExerciseDao){
    dao.insertExerciseInstance(
        ExerciseInstance(
            exerciseId = "Squat".hashCode(),
            reps = 5,
            sets = 5,
            startingWeight = 500
        )
    )
    dao.insertExerciseInstance(
        ExerciseInstance(
            exerciseId = "Bench Press".hashCode(),
            reps = 5,
            sets = 5,
            startingWeight = 350
        )
    )
    dao.insertExerciseInstance(
        ExerciseInstance(
            exerciseId = "Overhead Press".hashCode(),
            reps = 5,
            sets = 5,
            startingWeight = 200
        )
    )
    dao.insertExerciseInstance(
        ExerciseInstance(
            exerciseId = "Deadlift".hashCode(),
            reps = 5,
            sets = 1,
            startingWeight = 800
        )
    )
    dao.insertExerciseInstance(
        ExerciseInstance(
            exerciseId = "Barbell Row".hashCode(),
            reps = 5,
            sets = 5,
            startingWeight = 350
        )
    )
}

private suspend fun addExerciseToRoutine(dao: ExerciseDao){
    dao.insertExerciseInRoutine(
        ExerciseInRoutine(
            routineId = 1,
            exerciseInstanceId = 1,
            day = 0,
            exerciseOrder = 0
        )
    )
    dao.insertExerciseInRoutine(
        ExerciseInRoutine(
            routineId = 1,
            exerciseInstanceId = 2,
            day = 0,
            exerciseOrder = 1
        )
    )
    dao.insertExerciseInRoutine(
        ExerciseInRoutine(
            routineId = 1,
            exerciseInstanceId = 5,
            day = 0,
            exerciseOrder = 2
        )
    )

    dao.insertExerciseInRoutine(
        ExerciseInRoutine(
            routineId = 1,
            exerciseInstanceId = 1,
            day = 1,
            exerciseOrder = 0
        )
    )
    dao.insertExerciseInRoutine(
        ExerciseInRoutine(
            routineId = 1,
            exerciseInstanceId = 3,
            day = 1,
            exerciseOrder = 1
        )
    )
    dao.insertExerciseInRoutine(
        ExerciseInRoutine(
            routineId = 1,
            exerciseInstanceId = 4,
            day = 1,
            exerciseOrder = 2
        )
    )
}