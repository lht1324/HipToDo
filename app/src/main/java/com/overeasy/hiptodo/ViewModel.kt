package com.overeasy.hiptodo

import android.util.Log
import com.overeasy.hiptodo.model.ToDo
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class ViewModel() {
    var adapter = MainAdapter(this)
    var toDoList = ArrayList<ToDo?>()
    var publishSubject = PublishSubject.create<String>()

    init {
        publishSubject.subscribe { something ->
            toDoList.add(ToDo(something))
        }
    }

    fun onCreate() {
        toDoList.add(null)
        toDoList[0] = ToDo("ToDo", GregorianCalendar().timeInMillis)
        adapter.notifyDataSetChanged()
    }

    fun deleteToDo(position: Int) {
        toDoList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    fun dateChange(year: Int, month: Int, day: Int, position: Int) {
        val dateToday = GregorianCalendar()
        val dateChanged = GregorianCalendar(year, month, day)

        toDoList[position]!!.date = dateChanged.timeInMillis
        toDoList[position]!!.day = (dateToday.timeInMillis - dateChanged.timeInMillis) / 86400000 - 1
        adapter.notifyDataSetChanged()
    }

    fun showDeadline(toDo: ToDo): String {
        val dateToday = GregorianCalendar()
        val dateOrigin: Calendar? = if (toDo.date == null) null else GregorianCalendar()
        var year by Delegates.notNull<Boolean>()
        var month by Delegates.notNull<Boolean>()
        var day by Delegates.notNull<Boolean>()

        if (dateOrigin != null) {
            dateOrigin.timeInMillis = toDo.date!!
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