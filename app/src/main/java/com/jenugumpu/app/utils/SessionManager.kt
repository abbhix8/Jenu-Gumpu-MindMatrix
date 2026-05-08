package com.jenugumpu.app.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages user session and role using SharedPreferences
 */
class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "jenu_gumpu_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_USERNAME = "username"

        const val ROLE_COLLECTOR = "COLLECTOR"
        const val ROLE_BUYER = "BUYER"
    }

    fun saveSession(username: String, role: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USERNAME, username)
            putString(KEY_USER_ROLE, role)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserRole(): String? = prefs.getString(KEY_USER_ROLE, null)

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
