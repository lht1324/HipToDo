package com.overeasy.hiptodo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast

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

    fun onClickListener(view: View) {
        // 작동 확인.
        // 문제 : PopupWindow든 Dialog든 일단 띄워야 하는데 어떤 식으로 띄울까?
    }
    // 할 일, D-Day
    // D-Day는 String으로 저장했다 잘라서 표기하는 방식으로?
    // 4-2-2 하면 되잖아
    // Date나 Calendar도 있다던데
    // Calender 라이브러리 쓰고
    // 저장은 SQLite로.
    // 편집, 삭제 버튼
}