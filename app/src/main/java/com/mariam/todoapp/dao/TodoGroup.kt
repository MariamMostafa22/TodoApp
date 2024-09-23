package com.mariam.todoapp.dao

data class TodoGroup(
    var todos: MutableList<Todo>
)

data class Todo(
    var name: String,
    var isDone: Boolean
)
