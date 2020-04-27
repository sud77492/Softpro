package com.h.softpro.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.h.softpro.R
import com.h.softpro.helper.PreferenceHelper
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    internal var LoginURL = "http://10.0.2.2/softpro/index.php"
    private var etEmail: EditText? = null
    private var etpassword:EditText? = null
    private var btnlogin: Button? = null
    private var tvreg: TextView? = null
    private val LoginTask = 1
    private var preferenceHelper: PreferenceHelper? = null
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        preferenceHelper = PreferenceHelper(this)

        etEmail = findViewById<View>(R.id.etEmail) as EditText
        etpassword = findViewById<View>(R.id.etpassword) as EditText

        btnlogin = findViewById<View>(R.id.btn) as Button
        tvreg = findViewById<View>(R.id.tvreg) as TextView

        tvreg!!.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        btnlogin!!.setOnClickListener {
            try {
                login()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    }

    @Throws(IOException::class, JSONException::class)
    private fun login() {

        showSimpleProgressDialog(this@LoginActivity, null, "Loading...", false)

        try {

            Fuel.post(LoginURL, listOf("type" to  "login",
                 "email" to  etEmail!!.text.toString()
                , "password" to  etpassword!!.text.toString()
            )).responseJson { request, response, result ->
                Log.d("plzzzzz", result.get().content)
                onTaskCompleted(result.get().content,LoginTask)
            }
        } catch (e: Exception) {

        } finally {

        }
    }

    private fun onTaskCompleted(response: String, task: Int) {
        Log.d("responsejson", response)
        removeSimpleProgressDialog()  //will remove progress dialog
        when (task) {
            LoginTask -> if (isSuccess(response)) {
                saveInfo(response)
                Toast.makeText(this@LoginActivity, "Login Successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.finish()
            } else {
                Toast.makeText(this@LoginActivity, getErrorMessage(response), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveInfo(response: String) {
        preferenceHelper!!.putIsLogin(true)
        try {
            val jsonObject = JSONObject(response)
            if (jsonObject.getString("error") == "false") {
                val dataobj = jsonObject.getJSONObject("data")
                preferenceHelper!!.putName(dataobj.getString("name"))
                preferenceHelper!!.putEmail(dataobj.getString("email"))
                preferenceHelper!!.putDesignation(dataobj.getString("designation"))
//
//                for (i in 0 until dataArray.length()) {
//
//                    val dataobj = dataArray.getJSONObject(i)
//                    preferenceHelper!!.putName(dataobj.getString("name"))
//                    preferenceHelper!!.putHobby(dataobj.getString("hobby"))
//                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    fun isSuccess(response: String): Boolean {
        try {
            val jsonObject = JSONObject(response)
            return if (jsonObject.optString("error") == "false") {
                true
            } else {

                false
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return false
    }

    fun getErrorMessage(response: String): String {
        try {
            val jsonObject = JSONObject(response)
            return jsonObject.getString("message")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return "No data"
    }

    fun showSimpleProgressDialog(context: Context, title: String?, msg: String, isCancelable: Boolean) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg)
                mProgressDialog!!.setCancelable(isCancelable)
            }
            if (!mProgressDialog!!.isShowing) {
                mProgressDialog!!.show()
            }

        } catch (ie: IllegalArgumentException) {
            ie.printStackTrace()
        } catch (re: RuntimeException) {
            re.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                    mProgressDialog = null
                }
            }
        } catch (ie: IllegalArgumentException) {
            ie.printStackTrace()

        } catch (re: RuntimeException) {
            re.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}