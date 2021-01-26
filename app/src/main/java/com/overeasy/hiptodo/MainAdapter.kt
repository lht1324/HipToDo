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
        notifyItemMoved(beforePosition, afterPosition)
        // 움직이는 애니메이션이 종료된 후에 데이터가 변경되어야 하는데
        // 이렇게 하면 애니메이션 도중 데이터가 변경되고 애니메이션은 강제종료된다
        // notifyDataSetChanged()
        // 데이터바인딩으로 onClick()을 할 때 아이템에 있는 toDo를 사용하잖아?
        // 포지션을 옮긴 뒤 onClick()에 이전에 그 포지션에 있던 toDo가 사용된다는 건
        // 옮겨진 position이 View와 동기화되지 않았다는 소리야
        // 어디에서 안 된 거지?
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
            println("${toDo.something}'s position = $adapterPosition")
            // 포지션 변경이 반영되기 전에 터치해서 기존 아이템이 나온 거 같은데?
            // showDialog에 들어가는 거 어떤 어레이에서 나온 거냐
            // 그 어레이가 옮긴 다음 바로 변경 안 되니까 이런 거다
            // 아니지
            // onClick 할 때 들어가는 건 아이템에 데이터바인딩으로 묶인 거야
            // 포지션 변경 반영 전에 onClick에 묶인 게 들어가면 이전 게 들어가지
        }

        fun deleteToDo(toDo: ToDo) {
            onItemDeleted(toDo)
        }

        fun showDeadline(toDo: ToDo): String {
            val dateToday = GregorianCalendar()
            val dateOrigin: Calendar? = if (toDo.date == null) null else toDo.date

            return when {
                dateOrigin == null -> ""
                toDo.day == 0L -> "D-Day"
                dateOrigin.compareTo(dateToday) == 1 || dateOrigin.compareTo(dateToday) == -1 -> "D${String.format("%+d", toDo.day)}"
                else -> "Error!"
            }
        }
    }

    fun setItems(toDoList: ArrayList<ToDo>) {
        this.toDoList = toDoList
        notifyDataSetChanged()
    }

    fun println(data: String) {
        Log.d("MainAdapter", data)
    }
}