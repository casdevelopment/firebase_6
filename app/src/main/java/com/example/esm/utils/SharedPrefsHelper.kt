package com.example.esm.utils

import android.content.SharedPreferences

const val KEY_TOKEN = "token"
const val KEY_FMR= "fmr"
const val KEY_USER_ID = "userId"
const val KEY_MOBILE_CODE = "mobileCode"
const val KEY_STUDENT_PICTURE_STRING_CODE = "studentPictureString"
const val SHARE_PREF_NAME = "login"
const val KEY_DUMMY_LOGIN = "dummyLogin"

class SharedPrefsHelper(private val sharedPreferences: SharedPreferences){
    fun isLoggedIn(): Boolean{
        return sharedPreferences.getString(KEY_USER_ID,null)!= null
    }
    fun logout(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

   fun setFCMToken(token: String){
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }
    fun getFCMToken(): String? {
      return  sharedPreferences.getString(KEY_TOKEN,null)

    }
    fun setFMRURL(token: String){
        sharedPreferences.edit().putString(KEY_FMR, token).apply()
    }
    fun getFMRURL(): String? {
      return  sharedPreferences.getString(KEY_FMR,null)

    }
    fun setUserId(id: String){
        sharedPreferences.edit().putString(KEY_USER_ID, id).apply()

    }
    fun getUserId():String?{
        return sharedPreferences.getString(KEY_USER_ID,null)

    }
    fun setUserMobileCode(mblCode: Int){
        sharedPreferences.edit().putInt(KEY_MOBILE_CODE, mblCode).apply()

    }
    fun getUserMobileCode():Int{
        return sharedPreferences.getInt(KEY_MOBILE_CODE,0)
    }
    fun setStudentPictureString(str:String){
        sharedPreferences.edit().putString(KEY_STUDENT_PICTURE_STRING_CODE,null).apply()

    }
    fun getStudentPictureString(): String? {
        return sharedPreferences.getString(KEY_STUDENT_PICTURE_STRING_CODE,null)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

}
