package com.overeasy.simpletodo.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.overeasy.simpletodo.R
import com.overeasy.simpletodo.databinding.TodoDialogBinding
import com.overeasy.simpletodo.model.ToDo
import java.util.*

class ToDoDialog(private val mContext: Context) : Dialog(mContext) {
    private lateinit var binding: TodoDialogBinding
    private lateinit var toDoDialog: ToDoDialog
    private lateinit var layoutParams: WindowManager.LayoutParams
    lateinit var toDo: ToDo
    var something = ""
    var date = GregorianCalendar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.todo_dialog, null, false)

        binding.toDo = toDo
        binding.calendarView.minDate = System.currentTimeMillis()

        // CalendarView date change listener
        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            toDo.date = GregorianCalendar(year, month, day)
        }

        // Change toDo's 'something(String)' when the enter key is pressed during input in editText
        // editText에서 입력 중 엔터 키가 입력되면 toDo의 할 일 변경
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                toDo.something = p0.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        setContentView(binding.root)
        init()
    }

    // Dialog layout setting
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