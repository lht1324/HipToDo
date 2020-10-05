package com.overeasy.hiptodo

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import io.reactivex.subjects.PublishSubject
import okhttp3.internal.notify

class ViewModel(private var mContext: Context) {
    var adapter = MainAdapter(this)
    // var toDoList = ObservableArrayList<ToDo?>()
    var toDoList = ArrayList<ToDo?>()
    var publishSubject = PublishSubject.create<ToDo>()
    private lateinit var inputMethodManager : InputMethodManager

    init {
        publishSubject.subscribe { toDo ->
            toDoList.add(toDo)
        }
    }

    fun onCreate() {
        toDoList.add(null)
        toDoList[0] = ToDo("SomethingToDo", "D-Day")
        adapter.notifyDataSetChanged()
    }

    fun onResume() {

    }

    var addToDo = View.OnKeyListener { view, keyCode, keyEvent ->

        if ((view as EditText).text.isNotEmpty() && keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
            val toDo = ToDo(view.text.toString())
            publishSubject.onNext(toDo)
            view.text = null // EditText 초기화

            inputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            // 키보드 내리기
        }
        false
    }

    fun deleteToDo(position: Int) {
        toDoList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    fun println(data: String) {
        Log.d("ViewModel", data)
    }
}