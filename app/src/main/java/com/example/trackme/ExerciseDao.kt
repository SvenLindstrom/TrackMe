package com.example.trackme

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insertExerciseRecord(exerciseRecord: ExerciseRecord)

    @Insert
    suspend fun insertRepRecord(repRecord: RepRecord)

    @Upsert
    suspend fun updateExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise WHERE name=:name")
    fun getExercise(name:String):Exercise

    @Query("SELECT MAX(id) FROM ExerciseRecord")
    fun getExerciseId(): Int
    @Query("SELECT MAX(sessionId) FROM exerciserecord")
    fun getSessionId():Int
}