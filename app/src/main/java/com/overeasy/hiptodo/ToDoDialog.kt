package com.overeasy.hiptodo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.overeasy.hiptodo.databinding.TodoDialogBinding

class ToDoDialog(private val mContext: Context, private val position: Int, private val viewModel: ViewModel) : Dialog(mContext) {
    private lateinit var binding: TodoDialogBinding
    private lateinit var toDoDialog: ToDoDialog
    private lateinit var layoutParams: WindowManager.LayoutParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.todo_dialog, null, false)

        binding.toDo = viewModel.toDoList[position]
        binding.calendarView.minDate = System.currentTimeMillis()

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            viewModel.dateChange(year, month, day, position)
        }

        setContentView(binding.root)
        init()
    }

    private fun init() {
        layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.5f
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = layoutParams
        toDoDialog = this
    }
}