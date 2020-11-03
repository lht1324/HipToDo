package com.overeasy.hiptodo.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.overeasy.hiptodo.ToDo
import java.util.*

class Database(mContext: Context) {
    var database: SQLiteDatabase = mContext.openOrCreateDatabase("todo.db", MODE_PRIVATE, null)
    private var tableName = "toDoTable"

    init {
        database.execSQL(
            "create table if not exists $tableName ("
                    + " _id integer PRIMARY KEY autoincrement, "
                    + "something text, "
                    + "date long, ")
    }
    
    fun createTable() {
        if (database == null) {
            return
        }

        database.execSQL(
            "create table if not exists $tableName ("
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

    fun update(toDo: ToDo) { // 데이터 업데이트 시 사용

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

    fun query(position: Int): ToDo { // 정렬할 때 사용?
        // Rx랑 연동하면 괜찮을 거 같은데
        lateinit var something: String
        var date: Calendar? = null
        var cursor = database.rawQuery("select _id, something, date from $tableName", null)

        for (i in 0..cursor.count) {
            if (position == cursor.getInt(3)) {
                something = cursor.getString(1)

                if (cursor.getLong(2) != null) {
                    date = GregorianCalendar()
                    date.timeInMillis = cursor.getLong(2)
                }
                break
            }
            else
                cursor.moveToNext()
        }
        return if (date != null) ToDo(something, date)
        else ToDo(something)
    }

    fun restartApp(): ArrayList<ToDo> {
        var toDoList = ArrayList<ToDo>()
        lateinit var something: String
        var date: Calendar? = null

        var cursor = database.rawQuery("select _id, something, date from $tableName", null)

        for (i in 0..cursor.count) {
            something = cursor.getString(1)

            if (cursor.getLong(2) != null) {
                date = GregorianCalendar()
                date.timeInMillis = cursor.getLong(2)
                toDoList.add(ToDo(something, date))
            }

            else
                toDoList.add(ToDo(something))

            cursor.moveToNext()
        }

        return toDoList
    }

    fun println(data: String) {
        Log.d("Database", data)
    }
}