package com.overeasy.hiptodo.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.overeasy.hiptodo.R
import com.overeasy.hiptodo.ViewModel
import com.overeasy.hiptodo.databinding.ActivityMainBinding
import com.overeasy.hiptodo.function.ItemTouchHelperCallback
import com.overeasy.hiptodo.model.ToDo
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModel
    private lateinit var adapter: MainAdapter
    private lateinit var inputMethodManager: InputMethodManager

    // 해야 할 것
    // Rx 적용
    // 리사이클러뷰가 비었으면 추가하는 법을 보여준다
    // 처음 시작하면 사용법을 보여준다

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editor = getSharedPreferences("restartCheck", MODE_PRIVATE).edit()
        editor.putBoolean("restartApp", true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    override fun onBackPressed() {
        viewModel.publishSubject.onComplete()
        toast("종료하려면 뒤로 가기 버튼을 한 번 더 눌러주세요.")

        finish()
        super.onBackPressed()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, ViewModel.Factory(application)).get(ViewModel::class.java)
        viewModel.onCreate()
        adapter = MainAdapter()
        adapter.setItems(viewModel.toDoList)
        val backgroundMaker = BackgroundMaker(binding)
        val callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recyclerView)
        backgroundMaker.compareTime()

        binding.apply {
            // recyclerView.adapter = adapter
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.addItemDecoration(RecyclerViewDecoration(20))
            viewModel = viewModel
            lifecycleOwner = this@MainActivity
            editText.setOnKeyListener(addToDo)
        }

        adapter.onItemClicked.observe(this, { clickedModel ->
            showDialog(clickedModel)
        })
        adapter.onItemDeleted.observe(this, { toDo ->
            viewModel.deleteToDo(toDo)
        })
        adapter.onItemMoved.observe(this, { toDoList ->
            viewModel.movedItemsUpdate(toDoList[0], toDoList[1])
        })
        viewModel.toDoLiveData.observe(this, { toDoList ->
            adapter.setItems(toDoList)
            binding.recyclerView.adapter = adapter
        })
    }

    private var addToDo = View.OnKeyListener { view, keyCode, keyEvent ->
        if ((view as EditText).text.isNotEmpty() && keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
            viewModel.publishSubject.onNext(view.text.toString())

            view.text = null // EditText 초기화

            inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            // 키보드 내리기
            true
        }
        false
    }

    private fun showDialog(toDo: ToDo) {
        val toDoDialog = ToDoDialog(this)
        toDoDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        toDoDialog.toDo = toDo
        toDoDialog.setOnDismissListener {
            viewModel.updateToDo(toDoDialog.toDo)
        }
        toDoDialog.setCancelable(true)
        toDoDialog.show()
    }

    private fun println(data: String) {
        Log.d("MainActivity", data)
    }

    private fun toast(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT)
    }
}