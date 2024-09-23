package com.example.trackme.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.trackme.Database.Entitis.Exercise
import com.example.trackme.Database.Entitis.ExerciseInRoutine
import com.example.trackme.Database.Entitis.ExerciseInstance
import com.example.trackme.Database.Entitis.ExerciseRecord
import com.example.trackme.Database.Entitis.RepRecord
import com.example.trackme.Database.Entitis.Routine
import com.example.trackme.Database.joins.ExerciseJoin
import com.example.trackme.Database.joins.RecoredJoin

@Dao
interface ExerciseDao {
    @Upsert
    suspend fun insertRoutine(routine: Routine)

    @Upsert
    suspend fun insertExercise(exercise: Exercise)

    @Upsert
    suspend fun insertExerciseInRoutine(exerciseInRoutine: ExerciseInRoutine)

    @Insert
    suspend fun insertExerciseRecord(exerciseRecord: ExerciseRecord)

    @Insert
    suspend fun insertRepRecord(repRecord: RepRecord)

    @Upsert
    suspend fun insertExerciseInstance(exerciseInstance: ExerciseInstance)

//    @Query("SELECT * FROM exerciserecord WHERE sessionId=:sessionid")
//    suspend fun getExerciseRecForSession(sessionid: Int): List<ExerciseRecord>

    @Query("SELECT name, weight, complete FROM exerciserecord JOIN exerciseinstance ON exerciserecord.exerciseInstanceId = exerciseinstance.id JOIN exercise ON exercise.id = exerciseinstance.exerciseId WHERE exerciserecord.sessionId=:sessionid")
    suspend fun getExerciseRecForSession(sessionid: Int): List<RecoredJoin>

    @Query("SELECT name, sets, reps, startingWeight, exerciseInstanceId FROM ExerciseInstance JOIN exerciseinroutine ON exerciseinroutine.exerciseInstanceId = ExerciseInstance.id JOIN Exercise ON exercise.id = ExerciseInstance.exerciseId WHERE exerciseinroutine.routineId=:routineId AND exerciseinroutine.day=:day ORDER BY exerciseOrder")
    suspend fun getExercise(routineId:Int, day:Int): List<ExerciseJoin>

    @Query("SELECT * FROM routine WHERE name=:name LIMIT 1")
    suspend fun getRoutine(name:String): Routine

    @Query("SELECT * FROM exerciserecord WHERE exerciseInstanceId=:exerciseId ORDER by sessionId DESC LIMIT 3")
    suspend fun getLast3Rec(exerciseId: Int): List<ExerciseRecord>

    @Query("UPDATE exerciserecord SET complete= 1 WHERE exerciseInstanceId=:exerciseId AND sessionId=:sessionid")
    suspend fun updateRecored(exerciseId: Int, sessionid: Int)

    @Query("SELECT SUM(repComp) FROM reprecord WHERE exerciseInstanceId=:exerciseId AND sessionId=:sessionid")
    suspend fun checkCompletion(exerciseId: Int, sessionid: Int): Int

    @Query("SELECT MAX(setNum) FROM reprecord WHERE exerciseInstanceId=:exerciseId AND sessionId=:sessionid")
    suspend fun getSetsDone(exerciseId: Int, sessionid: Int): Int?

    @Update
    suspend fun updateRoutine(routine: Routine)

}