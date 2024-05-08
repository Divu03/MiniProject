package com.ligerinc.fruithub

// LoginStateUtil.kt

import android.content.Context

fun saveLoginState(context: Context, isLoggedIn: Boolean) {
    val sharedPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
}
