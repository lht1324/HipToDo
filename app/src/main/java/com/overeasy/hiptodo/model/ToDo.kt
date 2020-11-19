package com.overeasy.hiptodo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoTable")
class ToDo {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var something: String
    var date: Long?
    var day: Long? = null

    constructor(something: String) {
        this.something = something
        this.date = null
    }

    constructor(something: String, date: Long) {
        this.something = something
        this.date = date
    }
    // 할 일, D-Day
    // D-Day는 String으로 저장했다 잘라서 표기하는 방식으로?
    // 4-2-2 하면 되잖아
    // Date나 Calendar도 있다던데
    // Calender 라이브러리 쓰고
    // 저장은 SQLite로.
    // 편집, 삭제 버튼
}