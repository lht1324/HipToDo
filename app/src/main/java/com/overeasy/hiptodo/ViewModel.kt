package com.overeasy.hiptodo

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CalendarView
import android.widget.EditText
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.properties.Delegates

class ViewModel(private var mContext: Context) {
    var adapter = MainAdapter(this)
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
        toDoList[0] = ToDo("ToDo", GregorianCalendar())
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
            true
        }
        false
    }

    fun deleteToDo(position: Int) {
        toDoList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    fun openDialog(position: Int, toDo: ToDo) {
        val toDoDialog = ToDoDialog(mContext, toDo, position, this)
        toDoDialog.setCancelable(true)
        toDoDialog.show()
    }

    fun dateChange(view: CalendarView, year: Int, month: Int, day: Int, position: Int) {
        val dateToday = GregorianCalendar()
        val dateChanged = GregorianCalendar(year, month, day)

        toDoList[position]!!.date = dateChanged
        toDoList[position]!!.day = (dateToday.timeInMillis - dateChanged.timeInMillis) / 86400000 - 1
        adapter.notifyDataSetChanged()
    }

    fun showDeadline(toDo: ToDo): String {
        val dateToday = GregorianCalendar()
        val dateOrigin: Calendar? = toDo.date
        var year by Delegates.notNull<Boolean>()
        var month by Delegates.notNull<Boolean>()
        var day by Delegates.notNull<Boolean>()

        if (dateOrigin != null) {
            year = dateToday.get(Calendar.YEAR) == dateOrigin!!.get(Calendar.YEAR)
            month = dateToday.get(Calendar.MONTH) == dateOrigin!!.get(Calendar.MONTH)
            day = dateToday.get(Calendar.DATE) == dateOrigin!!.get(Calendar.DATE)
        }

        return when {
            dateOrigin == null -> ""
            dateOrigin.compareTo(dateToday) == 1 -> "D${String.format("%+d", toDo.day)}"
            year && month && day -> "D-Day"
            else -> "Error!"
        }
    }

    fun println(data: String) {
        Log.d("ViewModel", data)
    }
}