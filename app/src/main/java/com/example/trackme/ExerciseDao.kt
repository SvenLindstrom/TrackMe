package com.example.trackme

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Upsert
    suspend fun updateWeights(weights: Weights)

    @Query("SELECT * FROM weights WHERE current=1")
    fun getCurrentWeights(test:String): Weights

}