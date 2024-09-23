package com.mariam.todoapp.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database (entities = [TodoEntity::class, User::class], version = 4)
@TypeConverters(Converters::class)

abstract class TodoDB: RoomDatabase() {
    abstract fun todoDao(): TodoDAO

    companion object {
        private var INSTANCE: TodoDB? = null
        fun getDatabase(context: Context): TodoDB {
            return INSTANCE ?: synchronized(this) {val instance = Room.databaseBuilder(
                context.applicationContext,
                TodoDB::class.java,
                "todo_database")
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}