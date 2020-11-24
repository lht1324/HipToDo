package com.overeasy.hiptodo.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao {
    /* @Query("SELECT * FROM todoTable")
    fun getAll(): LiveData<ArrayList<ToDo>>

    // select _id, something, date from $tableName
    @Query("SELECT * FROM todoTable WHERE id IS :inputId")
    fun getToDo(inputId: Int): ToDo */

    @Insert
    fun insertToDo(entity: ToDo)

    @Update
    fun updateToDo(entity: ToDo)

    @Delete
    fun deleteToDo(entity: ToDo)

    // deleteAll : 초기화
}