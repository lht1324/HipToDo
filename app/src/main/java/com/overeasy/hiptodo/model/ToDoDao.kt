package com.overeasy.hiptodo.model

import androidx.room.*

@Dao
interface ToDoDao {
    @Query("SELECT * FROM toDoTable")
    fun getAll(): List<ToDo?>

    @Query("SELECT * FROM toDoTable WHERE id IS :inputId")
    fun getToDo(inputId: Int): ToDo

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(toDo: ToDo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(toDo: ToDo)

    @Delete
    fun delete(toDo: ToDo)

    // deleteAll : 초기화
}