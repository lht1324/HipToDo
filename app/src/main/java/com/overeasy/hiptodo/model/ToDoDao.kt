package com.overeasy.hiptodo.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.overeasy.hiptodo.ToDo

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todoTable")
    fun getAll(): LiveData<ArrayList<ToDo>>

    // select _id, something, date from $tableName
    @Query("SELECT * FROM todoTable WHERE id IS :inputId")
    fun getToDo(inputId: Int): ToDo

    @Insert
    fun insertToDo(toDo: ToDo)

    @Insert
    fun insertAll(toDoList: ArrayList<ToDo>)

    @Update
    fun updateToDo(toDo: ToDo)

    @Update
    fun updateAll(toDoList: ArrayList<ToDo>)

    @Delete
    fun deleteToDo(toDo: ToDo)

    @Delete
    fun deleteAll(toDoList: ArrayList<ToDo>)
}