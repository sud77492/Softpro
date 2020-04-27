package com.h.softpro.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.h.softpro.R
import com.h.softpro.helper.PreferenceHelper
import kotlinx.android.synthetic.main.welcome_activity.*

class WelcomeActivity : AppCompatActivity() {

    private var tvname: TextView? = null
    private var tvEmail:TextView? = null
    private var btnlogout: Button? = null
    private var preferenceHelper: PreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_activity)

            preferenceHelper = PreferenceHelper(this)

            tvEmail = findViewById<View>(R.id.tvEmail) as TextView
            tvname = findViewById<View>(R.id.tvname) as TextView
            btnlogout = findViewById<View>(R.id.btn) as Button

            tvname!!.text = "Welcome " + preferenceHelper!!.getNames()
            tvEmail!!.setText("Your Email is " + preferenceHelper!!.getEmail())

            btnlogout!!.setOnClickListener {
                preferenceHelper!!.putIsLogin(false)
                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this@WelcomeActivity.finish()
            }

        }
}