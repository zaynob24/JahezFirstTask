package com.example.jahezfirsttask.presentation.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.jahezfirsttask.R

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        intent= Intent(this, MainActivity::class.java)
        object: CountDownTimer(500,500){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                startActivity(intent)
                finish()
            }

        }.start()
    }
}