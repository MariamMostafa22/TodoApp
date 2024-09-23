package com.mariam.todoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mariam.todoapp.dao.TodoDB
import com.mariam.todoapp.dao.TodoEntity
import com.mariam.todoapp.dao.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel(application: Application): AndroidViewModel(application) {

    private val database: TodoDB = TodoDB.getDatabase(application.applicationContext)

    fun insertTodo(todo: TodoEntity, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.todoDao().insertTodo(todo)
                Log.d("Database", "Inserted Todo: $todo")
                withContext(Dispatchers.Main) {
                    onComplete(true)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
            }
        }
    }

    fun insertUser(user: User, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.todoDao().insertUser(user)
                Log.d("Database", "Inserted User: $user")
                withContext(Dispatchers.Main) {
                    onComplete(true)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
            }
        }
    }

    fun updateTodo(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.todoDao().updateTodo(todo)
            } catch (e: Exception) {
                Log.e("TodoViewModel", "Error updating todo", e)
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            database.todoDao().updateUser(user)
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            database.todoDao().deleteTodo(todo)
        }
    }

    fun getAllTodos(): LiveData<List<TodoEntity>> {
        return liveData(Dispatchers.IO) {
            val todos = database.todoDao().getAllTodos()
            emit(todos)
        }
    }

    fun getAllUsers(): LiveData<List<User>> {
        return liveData(Dispatchers.IO) {
            val users = database.todoDao().getAllUsers()
            emit(users)
        }
    }

    fun getTodoById(todoId: Int): LiveData<TodoEntity> {
        return liveData(Dispatchers.IO) {
            val todo = database.todoDao().getTodoById(todoId)
            emit(todo)
        }
    }

    fun getUserById(userId: Int): LiveData<User> {
        return liveData(Dispatchers.IO) {
            val user = database.todoDao().getUserById(userId)
            emit(user)
        }
    }

    fun getUserByEmail(email: String): LiveData<User> {
        return liveData(Dispatchers.IO) {
            val user = database.todoDao().getUserByEmail(email)
            emit(user)
        }
    }

    fun getUserTodoList(userId: Int): LiveData<List<TodoEntity>> {
        return liveData(Dispatchers.IO) {
            val todoList = database.todoDao().getUserTodoList(userId).todos
            emit(todoList)
        }
    }

    fun userExists(email: String): LiveData<Boolean> {
        return  liveData(Dispatchers.IO) {
            val exists = database.todoDao().userExists(email)
            emit(exists)
        }
    }

    fun deleteTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            database.todoDao().deleteAllTodos()
        }
    }

    fun deleteUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            database.todoDao().deleteAllUsers()
        }
    }
}