package com.github.rozag.redux.notes.logger

interface Logger {
    fun d(message: String)
    fun e(throwable: Throwable)
}