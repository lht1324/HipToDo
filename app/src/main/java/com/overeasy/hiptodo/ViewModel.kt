package com.overeasy.hiptodo

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates
import androidx.lifecycle.ViewModelProvider
import com.overeasy.hiptodo.model.ToDo
import com.overeasy.hiptodo.model.ToDoDao
import com.overeasy.hiptodo.model.ToDoDatabase

class ViewModel(application: Application) : ViewModel() {
    var adapter = MainAdapter(this)
    var toDoList = ArrayList<ToDo?>()
    var publishSubject = PublishSubject.create<String>()
    var toDoDao: ToDoDao = ToDoDatabase.getInstance(application)!!.toDoDao()

    init {
        publishSubject.subscribe { something ->
            val toDo = ToDo(something)
            toDoList.add(toDo)
            toDoDao.insert(toDo)
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ViewModel(application) as T
        }
    }

    fun onCreate() {
        if (toDoDao.getAll().isNotEmpty()) {
            toDoList.addAll(toDoDao.getAll())
            for (i in toDoList.indices) {
                println("toDoList[$i]")
                println("something = ${toDoList[i]!!.something}")
                println("date = ${toDoList[i]!!.date}")
            }
        }
        else {
            toDoList.add(null)
            toDoList[0] = ToDo("ToDo", GregorianCalendar())
            toDoDao.insert(toDoList[0]!!)
            // 얘는 제대로 들어갔구만
            // 날짜 바꾸고 넣는 거 구현 안 한 거 아니냐?
        }
        adapter.notifyDataSetChanged()
    }

    fun deleteToDo(position: Int) {
        toDoDao.delete(toDoList[position]!!)
        toDoList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    fun dateChange(year: Int, month: Int, day: Int, position: Int) {
        val dateToday = GregorianCalendar()
        val dateChanged = GregorianCalendar(year, month, day)

        toDoList[position]!!.date = dateChanged
        toDoList[position]!!.day = (dateToday.timeInMillis - dateChanged.timeInMillis) / 86400000 - 1
        toDoDao.update(toDoList[position]!!)
        adapter.notifyDataSetChanged()
    }

    fun showDeadline(toDo: ToDo): String {
        val dateToday = GregorianCalendar()
        val dateOrigin: Calendar? = if (toDo.date == null) null else toDo.date
        var year by Delegates.notNull<Boolean>()
        var month by Delegates.notNull<Boolean>()
        var day by Delegates.notNull<Boolean>()

        if (dateOrigin != null) {
            year = dateToday.get(Calendar.YEAR) == dateOrigin.get(Calendar.YEAR)
            month = dateToday.get(Calendar.MONTH) == dateOrigin.get(Calendar.MONTH)
            day = dateToday.get(Calendar.DATE) == dateOrigin.get(Calendar.DATE)
        }

        return when {
            dateOrigin == null -> ""
            dateOrigin.compareTo(dateToday) == 1 -> "D${String.format("%+d", toDo.day)}"
            year && month && day -> "D-Day"
            else -> "Error!"
        }
    }

    private fun println(data: String) {
        Log.d("ViewModel", data)
    }
}