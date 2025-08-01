package com.example.baseapp.utils

import android.content.Context

class PrefUtil(private val context: Context) {
    fun setInt(key: String?, value: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }
    fun setLong(key: String?, value: Long) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getInt(key: String?, defValue: Int): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getInt(key, defValue)
    }
    fun getlong(key: String?, defValue: Long): Long {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getLong(key, defValue)
    }
    fun setString(key: String?, value: String?) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun addNew(keyName: String?, valueName: String?,keyNumber: String?, valuePath: String?,keyProfile: String?, valueProfile: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putString(keyName, valueName)
        editor.putString(keyNumber, valuePath)
        editor.putInt(keyProfile, valueProfile)
        editor.apply()
    }



    fun getString(key: String?,defValue: String?=null): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getString(key, "null")
    }

    fun setBool(key: String?, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String?): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getBoolean(key, false)
    }

    fun getBool(key: String?, defaultValue: Boolean): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getBoolean(key, defaultValue)
    }

    //    public boolean getIsLinearLayout(String key) {

    companion object {
        val PREFS_NAME = "my_prefs"
        const val premiumKey = "PREMIUM"
        const val premiumCheck = "CHECK"
        fun setPremiumString(value: String, context: Context) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putString(premiumKey, value)
            editor.apply()
        }

        fun getPremium(context: Context): String? {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.getString(premiumKey, "")
        }

        fun setPremium(context: Context, value: Boolean) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putBoolean(premiumCheck, value)
            editor.apply()
        }

        fun isPremium(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.getBoolean("is_premium", false)
        }
    }
}