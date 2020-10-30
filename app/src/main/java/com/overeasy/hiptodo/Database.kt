package com.overeasy.hiptodo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class Database(mContext: Context) {
    var database: SQLiteDatabase = mContext.openOrCreateDatabase("todo.db", MODE_PRIVATE, null)
    lateinit var tableName: String

    fun createTable(name: String) {
        if (database == null) {
            return
        }

        tableName = name

        database.execSQL(
            "create table if not exists $tableName("
                    + " _id integer PRIMARY KEY autoincrement, "
                    + "something text, "
                    + "date long, ")
    }

    fun insert(toDo: ToDo) {
        if (database == null) {
            println("Database is not created.")
            return
        }
        if (tableName == null) {
            println("Table is not created.")
            return
        }

        database.execSQL("insert into $tableName"
        + "(something, date) "
        + " values "
        + "( ${toDo.something}, ${toDo.date?.timeInMillis} )"
        )
        // day는 상대적인 값이니 저장하지 않는다
    }

    fun update(toDo: ToDo) {

    }

    fun delete(toDo: ToDo) {
        if (database == null) {
            println("Database is not created.")
            return
        }
        if (tableName == null) {
            println("Table is not created.")
            return
        }

        database.execSQL("delete from $tableName where"
        + "something=${toDo.something}"
        + "date=${toDo.date}"
        )
    }

    fun query(toDo: ToDo) {

    }

    fun println(data: String) {
        Log.d("Database", data)
    }
}