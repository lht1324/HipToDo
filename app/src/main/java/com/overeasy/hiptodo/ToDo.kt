package com.overeasy.hiptodo

class ToDo {
    var something: String
    var deadline: String

    constructor(something: String) {
        this.something = something
        this.deadline = ""
    }

    constructor(something: String, deadline: String) {
        this.something = something
        this.deadline = deadline
    }
    // 할 일, D-Day
    // D-Day는 String으로 저장했다 잘라서 표기하는 방식으로?
    // 4-2-2 하면 되잖아
    // Date나 Calendar도 있다던데
    // Calender 라이브러리 쓰고
    // 저장은 SQLite로.
    // 편집, 삭제 버튼
}