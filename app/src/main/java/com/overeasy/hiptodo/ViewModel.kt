package com.overeasy.hiptodo

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
    // var adapter = MainAdapter(toDoList)
    var toDoList = ArrayList<ToDo>() // nullable 빼고 toDoList.add(ToDo(something) 되는지 보기
    var toDoLiveData: MutableLiveData<ArrayList<ToDo>> = MutableLiveData<ArrayList<ToDo>>()
    // D-Day가 나타나는 게 어떤 조건이지
    // 새로 켜서 DB에서 읽어오거나
    // null인 놈을 캘린더뷰로 설정하거나
    // 설정된 건 바로 바꾸면 된다
    // 리스너로 설정하면 되니까
    // 읽어왔을 때가 문제인 건데
    // 엔티티에 있는 date를 MutableLiveData로 바꿔버릴까?
    // MutableLiveData<Calendar?> 이러면 되잖아
    // Observer는 연결할 수 있나? 엔티티랑
    var publishSubject = PublishSubject.create<String>()
    var toDoDao: ToDoDao = ToDoDatabase.getInstance(application)!!.toDoDao()

    init {
        publishSubject.subscribe { something ->
            var toDo = ToDo(something)
            toDo.id = toDoDao.insert(toDo)
            toDoList.add(toDo)
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
                if (toDoList[i].date != null)
                    toDoList[i].day = (GregorianCalendar().timeInMillis - toDoList[i].date!!.timeInMillis) / 86400000 - 1
            }
        }
        else {
            // toDoList.add(null)
            toDoList.add(ToDo("ToDo"))
            // toDoList[0] = ToDo("ToDo")
            toDoList[0].date = GregorianCalendar()
            toDoDao.insert(toDoList[0])
            toDoLiveData.value = toDoList
        }
    }

    fun deleteToDo(toDo: ToDo) {
        for (i in toDoList.indices) {
            if (toDoList[i].id == toDo.id) {
                toDoList.removeAt(i)
                break
                // removeAt을 실행한 즉시 toDoList의 사이즈가 1 줄어든다
                // 그래서 에러엔 사이즈가 안 맞다고 나온다
            }
        }
        toDoDao.delete(toDo)
        toDoLiveData.value = toDoList
    }
    
    fun updateToDo(toDo: ToDo) {
        for (i in toDoList.indices) {
            if (toDoList[i].id == toDo.id) {
                toDoList[i] = toDo
                toDoList[i].day = (GregorianCalendar().timeInMillis - toDo.date!!.timeInMillis) / 86400000 - 1
            }
        }
        toDoDao.update(toDo)

        toDoLiveData.value = toDoList
    }

    private fun println(data: String) {
        Log.d("ViewModel", data)
    }
}