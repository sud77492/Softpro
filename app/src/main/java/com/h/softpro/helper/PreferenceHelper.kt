package com.h.softpro.helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(private val context: Context) {

    private val INTRO = "intro"
    private val NAME = "name"
    private val EMAIL = "email"
    private val DESIGNATIOM = "designation"
    private val app_prefs: SharedPreferences

    init {
        app_prefs = context.getSharedPreferences(
            "shared",
            Context.MODE_PRIVATE
        )
    }

    fun putIsLogin(loginorout: Boolean) {
        val edit = app_prefs.edit()
        edit.putBoolean(INTRO, loginorout)
        edit.commit()
    }

    fun getIsLogin(): Boolean {
        return app_prefs.getBoolean(INTRO, false)
    }

    fun putName(loginorout: String) {
        val edit = app_prefs.edit()
        edit.putString(NAME, loginorout)
        edit.commit()
    }

    fun getNames(): String? {
        return app_prefs.getString(NAME, "")
    }

    fun putEmail(loginorout: String) {
        val edit = app_prefs.edit()
        edit.putString(EMAIL, loginorout)
        edit.commit()
    }

    fun getEmail(): String? {
        return app_prefs.getString(EMAIL, "")
    }

    fun putDesignation(loginorout: String) {
        val edit = app_prefs.edit()
        edit.putString(DESIGNATIOM, loginorout)
        edit.commit()
    }

    fun getDesignation(): String? {
        return app_prefs.getString(DESIGNATIOM, "")
    }

}