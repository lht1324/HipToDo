package com.overeasy.hiptodo

import android.app.Activity
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding4.view.clicks
import com.overeasy.hiptodo.databinding.PopupWindowBinding
import com.overeasy.hiptodo.databinding.TodoItemBinding
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.subjects.PublishSubject

class ViewModel(private var mContext: Context) {
    var adapter = MainAdapter(this)
    var toDoList = ArrayList<ToDo?>()
    var publishSubject = PublishSubject.create<ToDo>()
    private lateinit var inputMethodManager : InputMethodManager

    init {
        publishSubject.subscribe { toDo ->
            toDoList.add(toDo)
        }
    }

    fun onCreate() {
        toDoList.add(null)
        toDoList[0] = ToDo("ToDo", "D-Day")
        adapter.notifyDataSetChanged()
    }

    fun onResume() {

    }

    var addToDo = View.OnKeyListener { view, keyCode, keyEvent ->

        if ((view as EditText).text.isNotEmpty() && keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
            val toDo = ToDo(view.text.toString())
            publishSubject.onNext(toDo)

            view.text = null // EditText 초기화

            inputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            // 키보드 내리기
        }
        false
    }

    fun deleteToDo(position: Int) {
        toDoList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    fun openPopupWindow(view: View, toDo: ToDo) {
        // 작동 확인.
        // Dialog로 하자... 원하던 그림이 안 나오네 ㅋㅋㅋㅋㅋ
        val binding: PopupWindowBinding = DataBindingUtil.setContentView(mContext as Activity, R.layout.popup_window)
        binding.toDo = toDo
        val popupWindow = PopupWindow(view)
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.popup_window, null)
        popupWindow.contentView = customView
        // popupWindow.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        popupWindow.isTouchable = true
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.showAsDropDown(view)
    }

    fun println(data: String) {
        Log.d("ViewModel", data)
    }
}