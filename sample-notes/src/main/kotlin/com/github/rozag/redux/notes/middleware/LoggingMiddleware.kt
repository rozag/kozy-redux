package com.github.rozag.redux.notes.middleware

import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.store.ReduxStore
import com.github.rozag.redux.notes.NotesAction
import com.github.rozag.redux.notes.AppState
import timber.log.Timber

class LoggingMiddleware(private val logLevel: Int)
    : ReduxMiddleware<AppState, NotesAction, ReduxStore<AppState, NotesAction>>() {

    override fun doAfterDispatch(store: ReduxStore<AppState, NotesAction>, action: NotesAction) {
        Timber.log(logLevel, "New state: ${store.getState()}")
    }

    override fun doBeforeDispatch(store: ReduxStore<AppState, NotesAction>, action: NotesAction) {
        Timber.log(logLevel, "Dispatching action: $action")
    }

}