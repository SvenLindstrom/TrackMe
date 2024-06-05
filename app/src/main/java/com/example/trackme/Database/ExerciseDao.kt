package com.example.trackme.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.trackme.Database.Entitis.Exercise
import com.example.trackme.Database.Entitis.ExerciseInRoutine
import com.example.trackme.Database.Entitis.ExerciseRecord
import com.example.trackme.Database.Entitis.RepRecord
import com.example.trackme.Database.Entitis.Routine
import java.util.Queue

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insertRoutine(routine: Routine)

    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Insert
    suspend fun insertExerciseInRoutine(exerciseInRoutine: ExerciseInRoutine)

    @Insert
    suspend fun insertExerciseRecord(exerciseRecord: ExerciseRecord)

    @Insert
    suspend fun insertRepRecord(repRecord: RepRecord)

    @Query("SELECT * FROM exerciserecord WHERE sessionId=:sessionid")
    suspend fun getExerciseRecForSession(sessionid: Int): List<ExerciseRecord>

    @Query("SELECT name FROM exercise JOIN exerciserecord ON exerciseId = id WHERE sessionId=:sessionid")
    suspend fun getExerciseRecForSessionTest(sessionid: Int): List<String>

    @Query("SELECT * FROM exercise JOIN exerciseinroutine ON id = exerciseId WHERE routineId=:routineId AND day=:day ORDER BY exerciseOrder")
    suspend fun getExercise(routineId:Int, day:Int): List<Exercise>

    @Query("SELECT * FROM routine WHERE name=:name LIMIT 1")
    suspend fun getRoutine(name:String): Routine

    @Query("SELECT * FROM exerciserecord WHERE exerciseId=:exerciseId ORDER by sessionId DESC LIMIT 3")
    suspend fun getLast3Rec(exerciseId: Int): List<ExerciseRecord>

    @Query("UPDATE exerciserecord SET complete= 1 WHERE exerciseId=:exerciseId AND sessionId=:sessionid")
    suspend fun updateRecored(exerciseId: Int, sessionid: Int)

    @Query("SELECT SUM(repComp) FROM reprecord WHERE exerciseId=:exerciseId AND sessionId=:sessionid")
    suspend fun checkCompletion(exerciseId: Int, sessionid: Int): Int

    @Update
    suspend fun updateRoutine(routine: Routine)

}