package com.github.rozag.redux.notes.prefs

import android.content.SharedPreferences

private const val KEY_IS_FIRST_LAUNCH = "is_first_launch"

class AndroidPrefs(private val prefs: SharedPreferences) : Prefs {

    override fun isFirstLaunch(): Boolean = prefs.getBoolean(KEY_IS_FIRST_LAUNCH, true)

    override fun onFirstLaunchPerformed() {
        prefs.edit().putBoolean(KEY_IS_FIRST_LAUNCH, false).apply()
    }

}