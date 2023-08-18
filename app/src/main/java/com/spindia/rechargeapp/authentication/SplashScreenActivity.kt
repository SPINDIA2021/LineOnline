package com.spindia.rechargeapp.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.spindia.rechargeapp.AdminMainActivity
import com.spindia.rechargeapp.NewMainActivity
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.utils.AppConstants

class SplashScreenActivity : AppCompatActivity() {
    // Splash screen timer
    private val SPLASH_TIME_OUT = 3000
    var tvLogoName: TextView? = null
    var mContext: Context = this@SplashScreenActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({ // This method will be executed once the timer is over
            // Start your app main activity

            val islogin: String =
                Preferences.getString(AppConstants.IS_LOGIN)
            if (islogin.equals("true")) {
                if (Preferences.getString(AppConstants.MOBILE).equals("9799754037")) {
                    val intent = Intent(this@SplashScreenActivity, AdminMainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@SplashScreenActivity, NewMainActivity::class.java)
                    startActivity(intent)
                }
                
            } else {
                val i = Intent(
                    this@SplashScreenActivity, LoginActivity
                    ::class.java
                )
                startActivity(i)
            }


            // close this activity
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }
}