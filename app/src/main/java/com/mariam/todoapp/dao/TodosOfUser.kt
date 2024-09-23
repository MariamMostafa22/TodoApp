package com.mariam.todoapp.dao

import androidx.room.Embedded
import androidx.room.Relation

data class TodosOfUser(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val todos: List<TodoEntity>
)
