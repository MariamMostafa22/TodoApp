package com.mariam.todoapp.dao

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun setTodoGroup(todos: List<Todo>): String {
        return Gson().toJson(todos) // Convert list to JSON
    }

    @TypeConverter
    fun getTodoGroup(jsonString: String): List<Todo> {
        return Gson().fromJson(jsonString, Array<Todo>::class.java).toList() // Convert JSON back to list
    }
}