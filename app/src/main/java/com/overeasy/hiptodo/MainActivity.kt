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

    // 해야 할 것
    // Rx, MVVM 패턴 적용
    // 캘린더뷰로 D-Day 설정
    // 꾹 누르면 이름이랑 날짜 설정하는 창 띄우기
    // 팝업윈도우는 카드뷰 쓰면 되겠구만
    // 삭제를 이미지 버튼으로 바꾸기
    // 제목이 영역을 넘기면 ...으로 줄여주기
    // 할 일 순서 정해야 한다
    // 캘린더 뷰(팝업 윈도우)
    // 정렬 (왼쪽을 터치하면 바 생성된 다음에 움직여서 정렬하는 걸로 할까?)
    // 체크박스는 누르면 이펙트가 뜨던데 그냥 체크박스 쓸까?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    override fun onBackPressed() {
        viewModel.publishSubject.onComplete()

        finish()
        super.onBackPressed()
    }

    private fun init() {
        viewModel = ViewModel(this)
        viewModel.onCreate()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    fun println(data: String) {
        Log.d("MainActivity", data)
    }
}