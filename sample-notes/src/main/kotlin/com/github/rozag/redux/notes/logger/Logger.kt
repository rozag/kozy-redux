package com.github.rozag.redux.notes.logger

interface Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, throwable: Throwable)
}