package com.overeasy.hiptodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.overeasy.hiptodo.databinding.ActivityMainBinding
import io.reactivex.Observable
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModel

    // 해야 할 것
    // Rx, MVVM 패턴 적용
    // 할 일 순서 정해야 한다
    // 아이템 생성 후 EditText가 초기화되는 게 아닌 줄바꿈이 한 칸 추가된다
    // 정렬 (왼쪽을 터치하면 바 생성된 다음에 움직여서 정렬하는 걸로 할까?)
    // D-Day가 정해졌으면 D-Day가 필요하지만 정해지지 않으면 굳이 신경쓸 필요가 없다

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        viewModel = ViewModel(this)
        viewModel.onCreate()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    fun println(data: String) {
        Log.d("MainActivity", data)
    }

    fun toast(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT)
    }
}