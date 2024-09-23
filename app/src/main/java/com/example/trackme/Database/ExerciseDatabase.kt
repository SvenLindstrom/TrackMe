package com.example.trackme.Database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trackme.Database.Entitis.Exercise
import com.example.trackme.Database.Entitis.ExerciseInRoutine
import com.example.trackme.Database.Entitis.ExerciseInstance
import com.example.trackme.Database.Entitis.ExerciseRecord
import com.example.trackme.Database.Entitis.RepRecord
import com.example.trackme.Database.Entitis.Routine

@Database(
    entities = [ExerciseRecord::class, RepRecord::class, Exercise::class, Routine::class, ExerciseInRoutine::class, ExerciseInstance::class],
    version = 1,
)
abstract class ExerciseDatabase:RoomDatabase() {
    abstract val dao: ExerciseDao
}