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

            return when {
                dateOrigin == null -> ""
                dateOrigin.compareTo(dateToday) == 1 -> "D${String.format("%+d", toDo.day)}"
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