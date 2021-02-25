package com.overeasy.hiptodo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.overeasy.hiptodo.R
import com.overeasy.hiptodo.databinding.ActivityIntroBinding


class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_intro)

        var binding: ActivityIntroBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_intro
        )

        val adapter = PagerAdapter(this)
        var fragments = ArrayList<IntroFragment>()

        fragments.add(IntroFragment("세 가지만 알면 됩니다.", "", R.drawable.three_finger, R.color.day_1))
        fragments.add(IntroFragment("1", "할 일을 입력한 뒤 ↵를 누르면 할 일이 추가됩니다.", R.drawable.phone1, R.color.day_2))
        fragments.add(IntroFragment("2", "항목을 누르면 할 일과 D-Day를 바꿀 수 있습니다.", R.drawable.phone2, R.color.day_3))
        fragments.add(IntroFragment("3", "꾹 눌러 옮기는 걸로 할 일의 순서를 바꿀 수 있습니다.", R.drawable.phone3, R.color.day_4))

        adapter.fragments = fragments

        binding.apply {
            val liveData = MutableLiveData<Int>()

            liveData.observe(this@IntroActivity, { position ->
                if (position == 0) {
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
            }

            textView2.text = (fragments.size - 1).toString()
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

    fun println(data: String) {
        Log.d("IntroActivity", data)
    }
}
