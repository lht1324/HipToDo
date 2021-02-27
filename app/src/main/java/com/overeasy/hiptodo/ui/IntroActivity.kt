package com.overeasy.hiptodo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.overeasy.hiptodo.R
import com.overeasy.hiptodo.databinding.ActivityIntroBinding


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

            liveData.observe(this@IntroActivity, { position ->
                if (position == 0 || position == adapter.fragments.size - 1) { // 4/4 지워야 한다
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

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    textView.text = viewPager.currentItem.toString()
                    liveData.value = viewPager.currentItem
                }
            })

            viewPager.adapter = adapter
            buttonDone.setOnClickListener { startMainActivity() }
            buttonNext.setOnClickListener {
                val position = viewPager.currentItem

                if (position < adapter.fragments.size) {
                    viewPager.setCurrentItem(position + 1, true)
                    liveData.value = viewPager.currentItem
                }
                if (position == adapter.fragments.size - 1)
                    startMainActivity()
            }

            textView2.text = (fragments.size - 2).toString()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun startMainActivity() {
        val intent = Intent(this@IntroActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedLast < 2000) {
            finish()
            return
        }

        Toast.makeText(this, "종료하려면 뒤로 가기 버튼을\n한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
        backPressedLast = System.currentTimeMillis()
    }

    fun println(data: String) {
        Log.d("IntroActivity", data)
    }
}
