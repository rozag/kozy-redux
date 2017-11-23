package com.github.rozag.redux.notes.prefs

interface Prefs {

    fun isFirstLaunch(): Boolean
    fun onFirstLaunchPerformed()

}