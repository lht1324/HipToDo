package com.overeasy.simpletodo

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.ViewModelProvider
import com.overeasy.simpletodo.model.ToDo
import com.overeasy.simpletodo.model.ToDoDao
import com.overeasy.simpletodo.model.ToDoDatabase

class ViewModel(application: Application) : ViewModel() {
    var toDoList = ArrayList<ToDo>()
    var toDoLiveData: MutableLiveData<ArrayList<ToDo>> = MutableLiveData<ArrayList<ToDo>>()
    var publishSubject = PublishSubject.create<String>()
    var toDoDao: ToDoDao = ToDoDatabase.getInstance(application)!!.toDoDao()

    init {
        // Receive what was typed in the editText of the activity
        // 액티비티에서 editText에 입력된 걸 전달 받음
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
        // When DB is not empty
        // DB가 비어있지 않을 때
        if (toDoDao.getAll().isNotEmpty()) {
            toDoList.addAll(toDoDao.getAll())
            
            for (i in toDoList.indices) {
                if (toDoList[i].date != null)
                    toDoList[i].day = (GregorianCalendar(
                        GregorianCalendar().get(Calendar.YEAR),
                        GregorianCalendar().get(Calendar.MONTH),
                        GregorianCalendar().get(Calendar.DAY_OF_MONTH)).timeInMillis - toDoList[i].date!!.timeInMillis) / 86400000

                // If the time is not set to 00:00, D-Day is twisted due to rounding when dividing by 86400000 after calculating timeInMillis
                // So use GregorianCalendar(year, month, day) format
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
                // The loop is terminated when the object created at the first run of the app is cleared after D-Day
                // 앱 첫 실행 시 생성되는 객체가 D-Day를 넘겨 지워지면 반복문을 종료한다

                // When the preceding for statement satisfies the condition, the size is reduced by 1
                // 선행 for문이 조건을 만족했을 때 size는 1 줄어든 상태
                for (i in toDoList.indices) {
                    if (toDoList[i].date != null && toDoList[i].day !! > 0L)
                        break
                    if (i == toDoList.size - 1 && (toDoList[i].date == null || (toDoList[i].date != null && toDoList[i].day!! <= 0L)))
                        finish = true
                }
                // If all non-null dates in toDoList are 0L or less
                // When opening the dialog after drag and drop
                // Object at the location before moving is read, not the object to be opened
                // toDoList 내부에 있는 null이 아닌 모든 date가 0L 이하라면
                // 드래그 앤 드랍으로 옮기고 나서 다이얼로그를 열 때
                // 열려는 객체가 아닌 옮기기 전의 위치에 있는 ToDo가 읽혀온다
            }
        }
        // The first time run the app
        // 앱을 처음 실행할 때
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

    // Delete object of DB and ViewModel internal list
    // DB와 뷰모델 내부 리스트에서 객체 삭제
    fun deleteToDo(toDo: ToDo) {
        for (i in toDoList.indices) {
            if (toDoList[i].id == toDo.id) {
                toDoList.removeAt(i)
                break
                // As soon as removeAt() is executed, the size of toDoList is reduced by 1
                // So the error says that the size is not correct
                // removeAt()을 실행한 즉시 toDoList의 사이즈가 1 줄어든다
                // 그래서 에러엔 사이즈가 안 맞는다고 나온다
            }
        }
        toDoDao.delete(toDo)
        toDoLiveData.value = toDoList
    }

    // Update object of DB and ViewModel internal list
    // DB와 뷰모델 내부 리스트의 객체 업데이트
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

    // DB and list update after drag and drop
    // 드래그 앤 드랍 후 DB와 리스트 업데이트
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

    // Position change function used when do drag and drop
    // 드래그 앤 드랍 시 사용되는 위치 변경 메소드
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

    // For log checking
    // 로그 확인용
    private fun println(data: String) {
        Log.d("ViewModel", data)
    }
}