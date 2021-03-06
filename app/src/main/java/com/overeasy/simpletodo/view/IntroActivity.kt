package com.overeasy.simpletodo.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.overeasy.simpletodo.R
import com.overeasy.simpletodo.databinding.ActivityIntroBinding


class IntroActivity : AppCompatActivity() {
    private var backPressedLast: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_intro)

        var binding: ActivityIntroBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

        val adapter = PagerAdapter(this)
        var fragments = ArrayList<IntroFragment>()

        fragments.add(IntroFragment("세 가지만 알면 끝이에요.", "", R.drawable.three_finger, R.color.dawn2_3, ""))
        fragments.add(IntroFragment("1", "할 일을 입력한 뒤 ↵를 누르면 할 일이 추가돼요.", R.drawable.phone1, R.color.day_3, ""))
        fragments.add(IntroFragment("2", "항목을 누르면 할 일과\nD-Day를 설정할 수 있어요.", R.drawable.phone2, R.color.sunset1_7, ""))
        fragments.add(IntroFragment("3", "꾹 눌러 옮기는 걸로 할 일의 순서를 바꿀 수 있어요.", R.drawable.phone3, R.color.sunset4_6, ""))
        fragments.add(IntroFragment("", "P.S.\nD-Day가 지나면 할 일은 저절로 사라져요!", R.drawable.phone4, R.color.night_6, "#936397"))

        adapter.fragments = fragments

        binding.apply {
            val liveData = MutableLiveData<Int>()

            // Show page number at the bottom according to page number
            // 현재 페이지에 따라 아래쪽의 페이지 번호를 보여주거나 가린다
            liveData.observe(this@IntroActivity, { position ->
                if (position == 0 || position == adapter.fragments.size - 1) {
                    textView.visibility = View.INVISIBLE
                    textView2.visibility = View.INVISIBLE
                    textView3.visibility = View.INVISIBLE
                }
                else {
                    textView.visibility = View.VISIBLE
                    textView2.visibility = View.VISIBLE
                    textView3.visibility = View.VISIBLE
                    textView.text = position.toString()
                }
            })

            // Change page number when changing page
            // 페이지 변경 시 페이지 수도 변경
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    textView.text = viewPager.currentItem.toString()
                    liveData.value = viewPager.currentItem
                }
            })

            viewPager.adapter = adapter

            // Skip button
            buttonSkip.setOnClickListener { startMainActivity() }
            // Next button
            buttonNext.setOnClickListener {
                val position = viewPager.currentItem

                // If it is not the last page, go to the next page
                // 마지막 페이지가 아니면 다음 페이지로 이동
                if (position < adapter.fragments.size) {
                    viewPager.setCurrentItem(position + 1, true)
                    liveData.value = viewPager.currentItem
                }
                // If it is last page, start MainActivity
                // 마지막 페이지면 MainActivity 시작
                if (position == adapter.fragments.size - 1)
                    startMainActivity()
            }

            // Total number of pages
            // 전체 페이지 수
            textView2.text = (fragments.size - 2).toString()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    // Start MainActivity
    private fun startMainActivity() {
        val pref = getSharedPreferences("restartCheck", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("restartApp", true)
        editor.apply()
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    // Press twice to exit
    // 2번 눌러 종료하기
    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedLast < 2000) {
            finish()
            return
        }

        Toast.makeText(this, "종료하려면 뒤로 가기 버튼을\n한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
        backPressedLast = System.currentTimeMillis()
    }

    // For log checking
    // 로그 확인용
    fun println(data: String) {
        Log.d("IntroActivity", data)
    }
}
