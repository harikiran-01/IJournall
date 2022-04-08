package com.hk.ijournal.utils

import android.content.Context
import android.content.SharedPreferences
import com.hk.ijournal.R

object SessionAuthManager {
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var userId: Long = 0

    fun setContext(ctx: Context) {
        pref = ctx.getSharedPreferences(ctx.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    //Create login session
    fun createUserLoginSession(uid: Long) {
        userId = uid
        editor.putLong("uid", userId)
        editor.apply()
    }

    //Clear session details
    fun logoutUser() {
        editor.clear()
        editor.apply()
    }

    // Check for login
    fun isUserLoggedIn(): Boolean {
        println("sessdeb " + getUID())
        return (getUID() > 0)
    }

    fun getUID(): Long {
        return pref.getLong("uid", 0)
    }
}