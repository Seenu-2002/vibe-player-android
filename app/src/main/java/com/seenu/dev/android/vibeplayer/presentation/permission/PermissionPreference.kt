package com.seenu.dev.android.vibeplayer.presentation.permission

import android.content.Context
import org.koin.core.annotation.Single

@Single
class PermissionPreference constructor(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "permission_prefs"
        private const val KEY_DONT_SHOW_AGAIN = "key_dont_show_again"
    }

    private val pref by lazy {
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun userDeclinedPermission(): Boolean {
        return pref
            .getBoolean(KEY_DONT_SHOW_AGAIN, false)
    }

    fun onUserDeclinedPermission(dontShowAgain: Boolean) {
        pref.edit()
            .putBoolean(KEY_DONT_SHOW_AGAIN, dontShowAgain)
            .apply()
    }
}