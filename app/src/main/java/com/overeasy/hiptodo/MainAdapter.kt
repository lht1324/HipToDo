package com.overeasy.hiptodo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.hiptodo.databinding.TodoItemBinding
import com.overeasy.hiptodo.model.ToDo
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class MainAdapter() : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private lateinit var toDoList: ArrayList<ToDo>
    var onItemClicked = MutableLiveData<ToDo>()
    var onItemDeleted = MutableLiveData<ToDo>()
    var onItemMoved = MutableLiveData<ArrayList<Int>>() // ArrayList 통으로 옮겨야 하는 거 아냐?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding,
            onItemClicked = { toDo ->
                onItemClicked.value = toDo
            },
            onItemDeleted = { toDo ->
                onItemDeleted.value = toDo
            }
        )
    }
    
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(toDoList[position]!!)
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    fun onItemDragMove(beforePosition: Int, afterPosition: Int) {
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
        val tempArrayList = ArrayList<Int>()
        tempArrayList.add(beforePosition)
        tempArrayList.add(afterPosition)
        onItemMoved.value = tempArrayList
        // 그냥 여기서 스왑하는 걸 실행해버릴까?
        // 아냐
        // 결국 필요한 건 beforePosition하고 afterPosition이잖아
        // 이 두 놈을 뷰모델에 보내야 한다는 건데
        // 여기서 실행하는 건 나쁘지 않은데
        // 혹시 액티비티에 있는 setItems랑 겹치진 않을까?
        notifyItemMoved(beforePosition, afterPosition)
    }

    fun changeMoveEvent() {

    }

    inner class ViewHolder(
        private val binding: TodoItemBinding,
        private val onItemClicked: (ToDo) -> Unit,
        private val onItemDeleted: (ToDo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(toDo: ToDo) {
            binding.viewHolder = this
            binding.toDo = toDo

            binding.executePendingBindings()
        }

        fun onClick(toDo: ToDo) {
            onItemClicked(toDo)
        }

        fun deleteToDo(toDo: ToDo) {
            onItemDeleted(toDo)
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
            // 다이얼로그 둥근 모서리 적용하니까
            // 둥근 모서리는 나오지도 않는다
            // D+로 출력하는 것도 넣어야겠다
            // 캘린더가 minDate 때문에 막히니까 이전 날짜 읽지를 못 하네
            // 일단 이건 해결한 거 같은데
            // 당일을 선택하면 D-Day가 안 나온다
            // 그냥 minDate 해제할까?
            // D-Day 앱으로 가는 거야

            
            return when {
                dateOrigin == null -> ""
                dateOrigin.compareTo(dateToday) == 1 || dateOrigin.compareTo(dateToday) == -1 -> "D${String.format("%+d", toDo.day)}"
                year && month && day -> "D-Day"
                else -> "Error!"
            }
        }
    }

    fun setItems(toDoList: ArrayList<ToDo>) {
        this.toDoList = toDoList
    }

    fun println(data: String) {
        Log.d("MainAdapter", data)
    }
}