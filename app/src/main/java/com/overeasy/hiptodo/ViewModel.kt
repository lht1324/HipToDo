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

class ViewModel(application: Application) : ViewModel() {
    var adapter = MainAdapter(this)
    var toDoList = ArrayList<ToDo?>()
    var publishSubject = PublishSubject.create<String>()
    // DB가 없으면 실행되고 있으면 안 된다
    // 뭐가 문제인 거지?
    // 일단 모델을 전부 제거했다
    // 뷰모델도 제대로 해 주고
    // DB, DAO, Entity 중 하나가 문제라는 건데
    // 세 놈 전부 빼니까 돌아간다
    // 하나씩 넣으면서 삽질 한 번 해 볼까
    // private var toDoDao = ToDoDatabase.getInstance(application)!!.toDoDao()

    init {
        publishSubject.subscribe { something ->
            val toDo = ToDo(something)
            toDoList.add(toDo)
            // toDoDao.insertToDo(toDo)
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ViewModel(application) as T
        }
    }

    fun onCreate() {
        toDoList.add(null)
        toDoList[0] = ToDo("ToDo", GregorianCalendar().timeInMillis)
        // toDoDao.insertToDo(toDoList[0]!!)
        adapter.notifyDataSetChanged()
    }

    fun deleteToDo(position: Int) {
        // toDoDao.deleteToDo(toDoList[position]!!)
        toDoList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    fun dateChange(year: Int, month: Int, day: Int, position: Int) {
        val dateToday = GregorianCalendar()
        val dateChanged = GregorianCalendar(year, month, day)

        toDoList[position]!!.date = dateChanged.timeInMillis
        toDoList[position]!!.day = (dateToday.timeInMillis - dateChanged.timeInMillis) / 86400000 - 1
        // toDoDao.updateToDo(toDoList[position]!!)
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