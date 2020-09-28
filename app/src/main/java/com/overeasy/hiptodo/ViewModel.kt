package com.overeasy.hiptodo

import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.databinding.ObservableArrayList
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject

class ViewModel {
    var adapter = MainAdapter(this)
    // var toDoList = ObservableArrayList<ToDo?>()
    var toDoList = ObservableArrayList<ToDo?>()
    var publishSubject = PublishSubject.create<ToDo>()

    constructor() {
        publishSubject.subscribe { toDo ->
            toDoList.add(toDo)
        }
        toDoList.add(null)
        toDoList[0] = ToDo("somethingToDo", "D-DAY")
    }
    /* constructor() {
        if (toDoList.isEmpty()) {
            toDoList = ObservableArrayList<ToDo>()
            toDoList[0] = ToDo("somethingToDo", "D-DAY")
        }
        if (adapter == null)
            adapter = MainAdapter(this)
    } */

    fun onCreate() {
        adapter.notifyDataSetChanged()
    }

    fun onResume() {

    }

    // 에러 목록
    // 1. 지우기가 안 눌린다
    // 2. toDo가 2개씩 생성된다. 일단 기존 것이 지워지거나 하진 않는다.
    // 3. 키보드가 열린 상태에서만 리사이클러뷰에 추가된 아이템이 보인다.
    // 4. 리사이클러뷰 width가 match_parent면 아이템 간격이 존나 넓어지고, wrap_content면 간격이 아이템 하나 정도 차이 난다.
    fun addItem(toDo: ToDo) {
        toDoList.add(toDo)
    }
    var addToDo = View.OnKeyListener { view, keyCode, keyEvent ->

        when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                val toDo = ToDo((view as EditText).text.toString())
                publishSubject.onNext(toDo)
                println("ToDo is made. ToDo's something is ${toDo.something}")
            }
        }
        true
        /* when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                val toDo = ToDo((view as EditText).text.toString())

                Observable.just(toDo)
                    .subscribe { toDo ->
                        toDoList.add(toDo)
                        println("ToDo is made. ToDo's something is ${toDo.something}")
                    }
            }
        }
        true */
    }
    /* fun addToDo(view: View, keyCode: Int, keyEvent: KeyEvent): View.OnKeyListener {


        binding.editText.setOnKeyListener { view, keyCode, keyEvent ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    val toDo = ToDo(binding.editText.text.toString())

                    Observable.just(toDo)
                        .subscribe { toDo ->
                            toDoList.add(toDo)
                        }
                    recyclerViewInit()
                }
            }
            true
        }
    } */

    fun println(data: String) {
        Log.d("ViewModel", data)
    }
}