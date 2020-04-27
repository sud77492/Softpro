package com.h.softpro.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.h.softpro.R
import com.h.softpro.helper.PreferenceHelper
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.register_activity.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    internal var RegisterURL = "http://10.0.2.2/softpro/index.php"
    private var etName: EditText? = null
    private var etEmail:EditText? = null
    private var etPassword:EditText? = null
    private var etConfirmPassword:EditText? = null
    private var btnregister: Button? = null
    private var tvlogin: TextView? = null
    private var preferenceHelper: PreferenceHelper? = null
    private val RegTask = 1
    private var designationVal : String = "";
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        preferenceHelper = PreferenceHelper(this)

        if (preferenceHelper!!.getIsLogin()) {
            val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
        }


        etName = findViewById<View>(R.id.etName) as EditText
        etEmail = findViewById<View>(R.id.etEmail) as EditText
        //spDesignation = findViewById<Spinner>(R.id.spDesignation) as Spinner
        etPassword = findViewById<View>(R.id.etPassword) as EditText
        etConfirmPassword = findViewById<View>(R.id.etConfirmPassword) as EditText
        btnregister = findViewById<View>(R.id.btn) as Button
        tvlogin = findViewById<View>(R.id.tvlogin) as TextView
        val spDesignation = findViewById<Spinner>(R.id.spDesignation)

        val designation = resources.getStringArray(R.array.Designation)

        if (spDesignation != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, designation)
            spDesignation.adapter = adapter

            spDesignation.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    //designation[position].toString();
                    Toast.makeText(this@MainActivity,
                        getString(R.string.selected_item) + " " +
                                "" + designation[position], Toast.LENGTH_SHORT).show()
                    designationVal = designation[position];
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        tvlogin!!.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btnregister!!.setOnClickListener {
            try {
                if(etPassword!!.text.toString().equals(etConfirmPassword!!.text.toString())){
                    register()
                }else{
                    Toast.makeText(this@MainActivity, "Your password is not matching!", Toast.LENGTH_SHORT).show()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


    }

    @Throws(IOException::class, JSONException::class)
    private fun register() {

        showSimpleProgressDialog(this@MainActivity, null, "Loading...", false)

        try {

            Fuel.post(RegisterURL, listOf("type" to  "register"
                ,"name" to  etName!!.text.toString()
                , "email" to  etEmail!!.text.toString()
                , "password" to  etPassword!!.text.toString()
                , "designation" to  designationVal
            )).responseJson { request, response, result ->
                Log.d("plzzzzz", result.get().content)
                onTaskCompleted(result.get().content,RegTask)
            }
        } catch (e: Exception) {

        } finally {

        }

    }

    private fun onTaskCompleted(response: String, task: Int) {
        Log.d("responsejson", response)
        removeSimpleProgressDialog()
        when (task) {
            RegTask -> if (isSuccess(response)) {
                //saveInfo(response)
                Toast.makeText(this@MainActivity, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.finish()
            } else {
                Toast.makeText(this@MainActivity, getErrorMessage(response), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveInfo(response: String) {
        preferenceHelper!!.putIsLogin(true)
        try {
            val jsonObject = JSONObject(response)
            if (jsonObject.getString("error") == "true") {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
//                val dataArray = jsonObject.getJSONArray("data")
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