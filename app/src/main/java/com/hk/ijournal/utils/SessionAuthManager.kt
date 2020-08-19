package com.hk.ijournal.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SessionAuthManager {
    // Shared Preferences reference
    private lateinit var pref: SharedPreferences

    // Editor reference for Shared preferences
    private lateinit var editor: SharedPreferences.Editor

    private var userId: Long = 0

    fun setContext(ctx: Context) {
        // Context
        pref = PreferenceManager.getDefaultSharedPreferences(ctx)
        editor = pref.edit()
        editor.apply()
    }

    //Create login session
    fun createUserLoginSession(uid: Long) {
        userId = uid
        editor.putLong("uid", userId)
        editor.apply()
    }

    /**
     * Clear session details
     */
    fun logoutUser() {
        editor.clear()
        editor.apply()
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    // Check for login
    fun isUserLoggedIn(): Boolean {
        println("sessdeb " + getUID())
        return (getUID() > 0)
    }

    fun getUID(): Long {
        return pref.getLong("uid", 0)
    }
}