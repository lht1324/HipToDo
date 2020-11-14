package com.overeasy.hiptodo

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
import com.overeasy.hiptodo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModel
    private lateinit var inputMethodManager: InputMethodManager

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
        // viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel = ViewModel()
        viewModel.onCreate()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.editText.setOnKeyListener(addToDo)
        // viewModel.rxjava(this)?
        // 뷰가 변경됐다는 정보를 생산한다 보면 되는 거 아닌가?
    }

    private var addToDo = View.OnKeyListener { view, keyCode, keyEvent ->
        if ((view as EditText).text.isNotEmpty() && keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
            val toDo = ToDo(view.text.toString())
            viewModel.publishSubject.onNext(toDo)

            view.text = null // EditText 초기화

            inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            // 키보드 내리기
            true
        }
        false
    }

    private fun println(data: String) {
        Log.d("MainActivity", data)
    }

    private fun toast(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT)
    }
}