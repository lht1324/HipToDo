package com.overeasy.hiptodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import com.overeasy.hiptodo.databinding.ActivityMainBinding
import io.reactivex.Observable
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private lateinit var toDoList: ObservableArrayList<ToDo>
    private lateinit var viewModel: ViewModel
    
    // Rx, MVVM 패턴 적용
    // 날짜를 정해야 할까?
    // 정해도 되고, 안 정해도 되잖아
    // 꾹 누르면 이름이랑 날짜 설정하는 거 보이게 할까?
    // 팝업윈도우는 카드뷰 쓰면 되겠구만
    // 처음엔 이름으로 설정하는 것만 있는 거지
    // 체크박스 삭제를 checkedTextView로 해야 하나, checkBox로 해야 하나?
    // + 버튼은 힙하지 않아

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    override fun onBackPressed() {
        viewModel.publishSubject.onComplete()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun init() {
        viewModel = ViewModel()
        viewModel.onCreate()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
    fun addToDo() {
        // editText에 입력 후 엔터 키를 누르면, 리사이클러뷰에 뿅 하고 할 일이 추가돼야 한다
    }

    fun println(data: String) {
        Log.d("MainActivity", data)
    }
}