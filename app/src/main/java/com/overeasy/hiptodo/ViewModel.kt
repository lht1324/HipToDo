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
            
            for (i in toDoList.indices) {
                if (toDoList[i].date != null)
                    toDoList[i].day = (GregorianCalendar(
                        GregorianCalendar().get(Calendar.YEAR),
                        GregorianCalendar().get(Calendar.MONTH),
                        GregorianCalendar().get(Calendar.DAY_OF_MONTH)).timeInMillis - toDoList[i].date!!.timeInMillis) / 86400000

                // 시간을 0시 0분으로 설정하지 않으면 timeInMillis 계산 후 86400000으로 나눌 때 반올림 때문에 숫자가 이상해진다
                // 그래서 GregorianCalendar(year, month, day) 형식을 사용
            }

            var finish = false

            while (!finish) {

                for (i in toDoList.indices) {
                    if (toDoList[i].date != null && toDoList[i].day!! > 0L) {
                        toDoDao.delete(toDoList[i])
                        toDoList.removeAt(i)
                        break
                    }
                }
                // delete 뒤 break하면 size는 1 줄어든다

                if (toDoList.size == 0)
                    break
                // 처음 1개 있던 게 D+가 되어서 지워지면 반복문을 종료한다

                for (i in toDoList.indices) { // 선행 for문이 조건을 만족했을 때 size는 1 줄어든 상태
                    if (toDoList[i].date != null && toDoList[i].day !! > 0L)
                        break
                    if (i == toDoList.size - 1 && (toDoList[i].date == null || (toDoList[i].date != null && toDoList[i].day!! <= 0L))) // < 0L이면 D데이도 포함 안 되잖아
                        finish = true
                }
                // toDoList 내부에 있는 null이 아닌 모든 date가 0L 이하라면
                // 드래그 앤 드랍으로 옮기고 나서 다이얼로그를 열면
                // 옮긴 뒤 기존의 위치에 있는 ToDo가 읽혀온다
            }
        }
        else {
            toDoList.add(ToDo("ToDo"))
            toDoList[0].date = GregorianCalendar(
                GregorianCalendar().get(Calendar.YEAR),
                GregorianCalendar().get(Calendar.MONTH),
                GregorianCalendar().get(Calendar.DAY_OF_MONTH)) // 당일 00시 00분으로 설정
            toDoList[0].day = 0L
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
                toDoList[i].day = if (toDo.date != null) (GregorianCalendar(
                    GregorianCalendar().get(Calendar.YEAR),
                    GregorianCalendar().get(Calendar.MONTH),
                    GregorianCalendar().get(Calendar.DAY_OF_MONTH)).timeInMillis - toDo.date!!.timeInMillis) / 86400000
                else null
            }
        }
        toDoDao.update(toDo)

        toDoLiveData.value = toDoList
    }

    fun movedItemsUpdate(beforePosition: Int, afterPosition: Int) {
        if (beforePosition < afterPosition) {
            for (i in beforePosition until afterPosition) {
                Collections.swap(toDoList, i, i + 1)
                toDoSwap(i, i + 1)
                toDoDao.update(toDoList[i])
                toDoDao.update(toDoList[i + 1])
            }
        }
        else {
            for (i in beforePosition downTo afterPosition + 1) {
                Collections.swap(toDoList, i, i - 1)
                toDoSwap(i, i - 1)
                toDoDao.update(toDoList[i])
                toDoDao.update(toDoList[i - 1])
            }
        }
    }

    private fun toDoSwap(num1: Int, num2: Int) {
        val tempSomething = toDoList[num1].something
        val tempDate = if (toDoList[num1].date == null) null else toDoList[num1].date
        val tempDay = if (toDoList[num1].day == null) null else toDoList[num1].day
        toDoList[num1].something = toDoList[num2].something
        toDoList[num1].date = toDoList[num2].date
        toDoList[num1].day = toDoList[num2].day
        toDoList[num2].something = tempSomething
        toDoList[num2].date = tempDate
        toDoList[num2].day = tempDay
    }

    private fun println(data: String) {
        Log.d("ViewModel", data)
    }
}