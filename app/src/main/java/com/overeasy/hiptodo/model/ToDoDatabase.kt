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
        private var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase? {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, ToDoDatabase::class.java, "todo_database")
                    .build() // Nullable
                INSTANCE
            }
        }
    }

    //디비객체제거
    open fun destroyInstance() {
        INSTANCE = null
    }
}