package com.overeasy.hiptodo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.hiptodo.databinding.TodoItemBinding

class MainAdapter(private val viewModel: ViewModel) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(viewModel, position)
    }

    override fun getItemCount(): Int {
        return viewModel.toDoList.size
    }

    inner class ViewHolder(private val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ViewModel, position: Int) {
            binding.viewModel = viewModel
            binding.position = position

            binding.executePendingBindings()
        }
    }
}