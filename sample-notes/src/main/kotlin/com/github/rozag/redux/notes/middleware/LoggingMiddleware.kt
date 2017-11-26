package com.github.rozag.redux.notes.middleware

import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.store.ReduxStore
import com.github.rozag.redux.notes.AppState
import com.github.rozag.redux.notes.NotesAction
import timber.log.Timber

class LoggingMiddleware(private val logLevel: Int, private val packageName: String)
    : ReduxMiddleware<AppState, NotesAction, ReduxStore<AppState, NotesAction>>() {

    override fun doBeforeDispatch(store: ReduxStore<AppState, NotesAction>, action: NotesAction) {
        val className = action.javaClass.canonicalName
        val simplifiedActionClassName = className.replace(packageName, "")
        Timber.log(logLevel, "Dispatching action: $simplifiedActionClassName")
    }

    override fun doAfterDispatch(store: ReduxStore<AppState, NotesAction>, action: NotesAction) {
        Timber.log(logLevel, "New state: ${store.getState()}")
    }

}