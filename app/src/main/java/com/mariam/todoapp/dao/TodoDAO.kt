package com.mariam.todoapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface TodoDAO {

    @Insert
    suspend fun insertTodo(todo: TodoEntity)

    @Insert
    suspend fun insertUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodo(todo: TodoEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("SELECT * FROM todos")
    suspend fun getAllTodos(): List<TodoEntity>

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * from todos WHERE id = :todoId")
    suspend fun getTodoById(todoId: Int): TodoEntity

    @Query("SELECT * from users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User

    @Query("SELECT * from users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserTodoList(userId: Int): TodosOfUser

    @Query("SELECT EXISTS (SELECT 1 FROM users WHERE email = :email)")
    suspend fun userExists(email: String): Boolean

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM todos")
    suspend fun deleteAllTodos()
}