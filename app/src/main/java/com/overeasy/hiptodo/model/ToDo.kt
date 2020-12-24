package com.overeasy.hiptodo.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "toDoTable")
class ToDo {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var something: String
    var date: Calendar?

    @Ignore
    var day: Long? = null

    constructor(something: String) {
        this.something = something
        this.date = null
    }

    /* @Ignore
    constructor(something: String, date: Calendar) {
        this.something = something
        this.date = date
    } */
}