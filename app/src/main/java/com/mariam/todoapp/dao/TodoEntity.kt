package com.mariam.todoapp.dao

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "todos",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )])
open class TodoEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var color: Int,
    val userId: Int,
    var type: TodoType,

    @Embedded var simpleTodo: SimpleTodo? = null,
    @Embedded var groupTodo: TodoGroup? = null
)

enum class TodoType {
    SIMPLE_TODO, TODO_GROUP
}
