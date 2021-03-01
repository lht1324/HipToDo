package com.overeasy.hiptodo.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.hiptodo.databinding.TodoItemBinding
import com.overeasy.hiptodo.model.ToDo
import java.util.*
import kotlin.collections.ArrayList

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

    fun onItemMove(beforePosition: Int, afterPosition: Int) {
        if (beforePosition < afterPosition) {
            for (i in beforePosition until afterPosition) {
                Collections.swap(toDoList, i, i + 1)
            }
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