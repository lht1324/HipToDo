package com.overeasy.hiptodo.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.overeasy.hiptodo.R

class SplashActivity : Activity() {
    override fun onCreate(savedStatedBundle: Bundle?) {
        super.onCreate(savedStatedBundle)

        setContentView(R.layout.activity_splash)

        Thread.sleep(3000)

        val pref = getSharedPreferences("restartCheck", MODE_PRIVATE)
        val restartApp = pref.getBoolean("restartApp", false)

        if (restartApp)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        else
            startActivity(Intent(this@SplashActivity, IntroActivity::class.java))

        finish()
    }

    override fun onBackPressed() {
        //초반 플래시 화면에서 넘어갈 때 뒤로가기 버튼 못 누르게 함
    }

    private fun println(data: String) {
        Log.d("SplashActivity", data)
    }
}