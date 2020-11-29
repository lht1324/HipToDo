package com.overeasy.hiptodo.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ToDo::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null

        fun getInstance(context: Context) : ToDoDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoDatabase::class.java,
                        "todo_database")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
/* companion object {
       @Volatile
       private var INSTANCE: ToDoDatabase? = null

       fun getInstance(context: Context) : ToDoDatabase? {
           if (INSTANCE == null) {
               synchronized(this) {
                   INSTANCE = Room.databaseBuilder(
                       context.applicationContext,
                       ToDoDatabase::class.java,
                       "todo_database"
                   ).build()
               }
           }
           return INSTANCE
       }
   } */