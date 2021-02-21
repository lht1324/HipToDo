package com.overeasy.hiptodo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

class SplashActivity : Activity() {
    override fun onCreate(savedStatedBundle: Bundle?) {
        super.onCreate(savedStatedBundle)

        setContentView(R.layout.activity_splash)

        Thread.sleep(2500)

        val pref = getSharedPreferences("restartCheck", MODE_PRIVATE)
        val restartApp = pref.getBoolean("restartApp", false)

        println("restartApp= $restartApp")
        if (restartApp)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        else
            startActivity(Intent(this@SplashActivity, IntroActivity::class.java))

        finish()
    }

    override fun onBackPressed() {
        //초반 플래시 화면에서 넘어갈 때 뒤로가기 버튼 못 누르게 함
    }

    fun println(data: String) {
        Log.d("SplashActivity", data)
    }
}