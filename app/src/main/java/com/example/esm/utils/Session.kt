package com.example.esm.utils

import android.content.Context

object Session {
    private var SHARED_PREF_NAME = "login"
    private const val KEY_USER_INFO = "userInfo"
    private const val TOKEN = "token"


    fun isLoggedIn(mCtx: Context): Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USER_INFO, null) != null
    }

    fun logout(mCtx: Context) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

    }

    fun userInfo(userInfo: String?, mCtx: Context) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_INFO, userInfo)
        editor.apply()
    }

    fun getUserInfo(mCtx: Context) : String? {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USER_INFO, null)
    }

    fun saveToken(token: String?, mCtx: Context) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getToken(mCtx: Context): String? {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN, null)
    }

}