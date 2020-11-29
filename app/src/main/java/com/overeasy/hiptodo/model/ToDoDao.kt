package com.overeasy.hiptodo.model

import androidx.room.*

@Dao
interface ToDoDao {
    @Query("SELECT * FROM toDoTable")
    fun getAll(): ArrayList<ToDo>

    @Query("SELECT * FROM toDoTable WHERE id IS :inputId")
    fun getToDo(inputId: Int): ToDo

    // impl이 생성되지 않는다
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(toDo: ToDo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(toDo: ToDo)

    @Delete
    fun delete(toDo: ToDo)

    // deleteAll : 초기화
}