package com.github.rozag.redux.notes.logger

import android.util.Log

class AndroidLogger : Logger {

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, throwable: Throwable) {
        Log.e(tag, "", throwable)
    }

}