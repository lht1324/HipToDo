package com.overeasy.hiptodo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment


class IntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(AppIntroFragment.newInstance("3가지만 알면 됩니다.", "", R.drawable.three_finger, ContextCompat.getColor(this, R.color.dawn1_3)))
        addSlide(AppIntroFragment.newInstance("1", "①에 할 일을 입력한 뒤 ↵을 터치하면", R.drawable.phone1, ContextCompat.getColor(this, R.color.day_4)))
        addSlide(AppIntroFragment.newInstance("1", "할 일이 새로 생겨납니다.", R.drawable.phone2, ContextCompat.getColor(this, R.color.day_4)))
        addSlide(AppIntroFragment.newInstance("2", "원하는 항목을 터치하면", R.drawable.phone3, ContextCompat.getColor(this, R.color.sunset3_5)))
        addSlide(AppIntroFragment.newInstance("2", "할 일을 바꾸거나 D-Day를 정할 수 있습니다.", R.drawable.phone4, ContextCompat.getColor(this, R.color.sunset3_5)))
        // addSlide(AppIntroFragment.newInstance("2", "할 일을 바꾸거나 D-Day를 정할 수 있습니다.", R.drawable.phone5, ContextCompat.getColor(this, R.color.sunset3_5)))
        addSlide(AppIntroFragment.newInstance("2", "D-Day가 지나면 할 일은 자동으로 사라집니다.", R.drawable.phone6, ContextCompat.getColor(this, R.color.sunset3_5)))
        addSlide(AppIntroFragment.newInstance("3", "항목을 길게 눌러 옮기면\n순서를 바꿀 수 있습니다", R.drawable.phone7, ContextCompat.getColor(this, R.color.night_7)))
        // addSlide(splash) * n
    }
    override fun onSkipPressed(currentFragment: Fragment) {
        super.onSkipPressed(currentFragment)
        startMainActivity()
    }

    override fun onDonePressed(currentFragment: Fragment) {
        super.onDonePressed(currentFragment)
        startMainActivity()
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun startMainActivity() {
        val intent = Intent(this@IntroActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}