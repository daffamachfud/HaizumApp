package com.onoh.haizumapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.onoh.haizumapp.R
import com.onoh.haizumapp.ui.auth.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val  splashTimeout:Long = 3000 // 1 sec

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this, LoginActivity::class.java))

            // close this activity
            finish()
        }, splashTimeout)
    }
}
