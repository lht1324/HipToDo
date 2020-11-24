package com.overeasy.hiptodo.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDo::class], version = 1)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao

    companion object {
        @Volatile
        private var instance: ToDoDatabase? = null

        fun getInstance(context: Context) : ToDoDatabase? {
            if (instance == null) {
                synchronized (ToDoDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context,
                        ToDoDatabase::class.java,
                        "todo_database"
                    )
                    .build()
                }
            }
            return instance
        }
    }
}