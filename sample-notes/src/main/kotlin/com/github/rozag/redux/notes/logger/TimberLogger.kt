package com.github.rozag.redux.notes.logger

import timber.log.Timber

class TimberLogger : Logger {

    override fun d(message: String) {
        Timber.d(message)
    }

    override fun e(throwable: Throwable) {
        Timber.e(throwable)
    }

}