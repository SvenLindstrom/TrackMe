package com.example.trackme
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExerciseRecord::class, RepRecord::class, Exercise::class],
    version = 1,
)
abstract class ExerciseDatabase:RoomDatabase() {
    abstract val dao:ExerciseDao
}