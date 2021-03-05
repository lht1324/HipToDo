package com.overeasy.hiptodo.view

import android.content.Intent
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
    private var backPressedLast: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1500)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check whether it is the first run or rerun
        // 첫 실행인지 재실행인지 체크
        val pref = getSharedPreferences("restartCheck", MODE_PRIVATE)
        val restartApp = pref.getBoolean("restartApp", false)

        if (!restartApp)
            startIntroActivity()

       binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    override fun onBackPressed() {
        viewModel.publishSubject.onComplete()

        // Exit if touch once more before 2 secs
        // 2초 전에 누르면 종료
        if (System.currentTimeMillis() - backPressedLast < 2000) {
            finish()
            return
        }

        Toast.makeText(this, "종료하려면 뒤로 가기 버튼을\n한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
        backPressedLast = System.currentTimeMillis()
    }

    override fun onStop() {
        super.onStop()

        // Record it is already run
        // 처음 실행이 아니란 것 기록
        val editor = getSharedPreferences("restartCheck", MODE_PRIVATE).edit()
        editor.putBoolean("restartApp", true)
        editor.apply()
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

        // Set background
        // 배경 세팅
        backgroundMaker.compareTime()

        // Initialize data binding
        // 데이터바인딩 초기화
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.addItemDecoration(RecyclerViewDecoration(20))
            viewModel = viewModel
            lifecycleOwner = this@MainActivity
            editText.setOnKeyListener(addToDo)
            
            helpButton.setOnClickListener {
                startIntroActivity()
            }
        }

        // Open Dialog when touch item
        // 항목 누르면 Dialog 열기
        adapter.onItemClicked.observe(this, { clickedModel ->
            showDialog(clickedModel)
        })
        // Delete item
        // 항목 삭제
        adapter.onItemDeleted.observe(this, { toDo ->
            viewModel.deleteToDo(toDo)
        })
        // Move item
        // 항목 이동
        adapter.onItemMoved.observe(this, { toDoList ->
            viewModel.movedItemsUpdate(toDoList[0], toDoList[1])
        })
        // Reset adapter when list contents are changed
        // List 내용 변경 시 어댑터 재설정
        viewModel.toDoLiveData.observe(this, { toDoList ->
            adapter.setItems(toDoList)
            binding.recyclerView.adapter = adapter
        })
    }

    // Add item when press Enter key in editText
    // editText에서 엔터 누르면 항목 추가
    private var addToDo = View.OnKeyListener { view, keyCode, keyEvent ->
        if ((view as EditText).text.isNotEmpty() && keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
            viewModel.publishSubject.onNext(view.text.toString())

            // Initialize editText
            // editText 초기화
            view.text = null

            // Hide keyboard
            // 키보드 내리기
            inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            true
        }
        false
    }

    // Initialize Dialog
    // Dialog 초기화
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

    // Start IntroActivity
    private fun startIntroActivity() {
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }

    // For log checking
    // 로그 확인용
    private fun println(data: String) {
        Log.d("MainActivity", data)
    }
}