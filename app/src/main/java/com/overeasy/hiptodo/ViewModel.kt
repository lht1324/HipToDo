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
    var toDoList = ArrayList<ToDo>()
    var toDoLiveData: MutableLiveData<ArrayList<ToDo>> = MutableLiveData<ArrayList<ToDo>>()
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

            /*
            val tempPositions = ArrayList<Int>()
            for (i in toDoList.indices) {
                if (toDoList[i].date != null && toDoList[i].date!!.compareTo(GregorianCalendar()) == -1) {
                    tempPositions.add(i)
                    // 이걸 어떻게 하지?
                    // removeAt으로 지우면 반복문 뻑나잖아
                    // date가 이전 날짜인 position을 전부 기록한 다음에
                    // 반복문 돌려서 없애버리는 걸로 할까?
                    // tempPosition하고 비교하는 방법을 찾아야 한다
                }
            }
            for (i in tempPositions.indices) {
                toDoList.removeAt(tempPositions[i])

                for (j in i + 1..tempPositions.size) {
                    tempPositions[j] -= 1
                }
                // 근데 이것도 결국 안 되지 않냐?
                // 한 번 할 때마다 -1씩 해줘야 하나?
                // removeAt을 하고 나면 toDoList.size가 바뀌니까 tempPositions의 원소들도 쓸모가 없어진다
                // tempPositions[i]도 사용하고 remove해야 하나?
                // 그럼 반복문은 어떻게 돌리고?
                // toDoList remove한 거는 중첩 반복으로 어찌 되는 거 아녔어?
                // 일단 D+로 놓고 나중에 하는 걸로 하자
                // 테스트 하려면 D+가 있어야 하는데 그거 하려면 하루 걸린다
                // 앱 이름 바꾸는 걸로 여러 개 깔면 될 거 같긴 한데 결국 그것도 시간 걸려
                // 일단 디자인부터 끝내고 보자
            } */
            for (i in toDoList.indices) {
                if (toDoList[i].date != null)
                    toDoList[i].day = (GregorianCalendar().timeInMillis - toDoList[i].date!!.timeInMillis) / 86400000 - 1
                // 여기서 day 만들어서 넣어준다 해도
                // 어떤 미친 놈이 다이얼로그 열고 아무 것도 안 하고 그냥 꺼버리면?
                // if로 저장된 date 체크해서 일단 day 만들고 다이얼로그 닫혔을 때 이전 날짜면 day 유지하고 아니면 바꾸는 걸로 하자
            }
        }
        else {
            toDoList.add(ToDo("ToDo"))
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

    fun movedItemsUpdate(beforePosition: Int, afterPosition: Int) {
        if (beforePosition < afterPosition) {
            for (i in beforePosition until afterPosition)
                Collections.swap(toDoList, i, i + 1)
            // ViewModel도 바꿔줘야 한다
        }
        else {
            for (i in beforePosition downTo afterPosition + 1) {
                Collections.swap(toDoList, i, i - 1)
            }
        }
        for (i in toDoList.indices)
        toDoDao.updateAll(toDoList)
        // 안 되네
        // 반복문으로 update 돌리는 건 좀 무식한 느낌이고
    }

    private fun println(data: String) {
        Log.d("ViewModel", data)
    }
}