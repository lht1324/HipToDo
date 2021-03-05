package com.overeasy.simpletodo.model

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
    // If two or more constructors are defined in an entity of Room, kaptDebugError is occurred.
    // Room의 엔티티에서 constructor를 2개 이상 정의하면 kaptDebugError가 발생한다
}